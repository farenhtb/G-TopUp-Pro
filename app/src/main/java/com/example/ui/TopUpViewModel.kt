package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.TopUpTransaction
import com.example.data.TransactionRepository
import com.example.model.GameInfo
import com.example.model.GamePackage
import com.example.model.PaymentMethod
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TopUpViewModel(private val repository: TransactionRepository) : ViewModel() {

    // Persistent transactions from Room database
    val allTransactions: StateFlow<List<TopUpTransaction>> = repository.allTransactions
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Form selection state
    private val _selectedGame = MutableStateFlow<GameInfo?>(null)
    val selectedGame: StateFlow<GameInfo?> = _selectedGame.asStateFlow()

    private val _selectedPackage = MutableStateFlow<GamePackage?>(null)
    val selectedPackage: StateFlow<GamePackage?> = _selectedPackage.asStateFlow()

    private val _selectedPaymentMethod = MutableStateFlow<PaymentMethod?>(null)
    val selectedPaymentMethod: StateFlow<PaymentMethod?> = _selectedPaymentMethod.asStateFlow()

    private val _userIdInput = MutableStateFlow("")
    val userIdInput: StateFlow<String> = _userIdInput.asStateFlow()

    private val _zoneIdInput = MutableStateFlow("")
    val zoneIdInput: StateFlow<String> = _zoneIdInput.asStateFlow()

    // Error messages
    private val _formError = MutableStateFlow<String?>(null)
    val formError: StateFlow<String?> = _formError.asStateFlow()

    // Active pending payment state
    private val _activeTransaction = MutableStateFlow<TopUpTransaction?>(null)
    val activeTransaction: StateFlow<TopUpTransaction?> = _activeTransaction.asStateFlow()

    private val _paymentCountdown = MutableStateFlow(300) // 5 minutes in seconds
    val paymentCountdown: StateFlow<Int> = _paymentCountdown.asStateFlow()

    private var timerJob: Job? = null

    fun selectGame(game: GameInfo) {
        _selectedGame.value = game
        _selectedPackage.value = null
        _formError.value = null
    }

    fun selectPackage(pkg: GamePackage) {
        _selectedPackage.value = pkg
        _formError.value = null
    }

    fun selectPaymentMethod(method: PaymentMethod) {
        _selectedPaymentMethod.value = method
        _formError.value = null
    }

    fun updateUserId(userId: String) {
        _userIdInput.value = userId
        _formError.value = null
    }

    fun updateZoneId(zoneId: String) {
        _zoneIdInput.value = zoneId
        _formError.value = null
    }

    fun clearForm() {
        _selectedPackage.value = null
        _selectedPaymentMethod.value = null
        _userIdInput.value = ""
        _zoneIdInput.value = ""
        _formError.value = null
    }

    // Triggered when a transaction is submitted
    fun initiateTopUp(onSuccess: (Int) -> Unit) {
        val game = _selectedGame.value
        val pkg = _selectedPackage.value
        val payment = _selectedPaymentMethod.value
        val userId = _userIdInput.value
        val zoneId = _zoneIdInput.value

        if (game == null) {
            _formError.value = "Pilih game terlebih dahulu"
            return
        }
        if (userId.trim().isEmpty()) {
            _formError.value = "Masukkan ${game.idFieldLabel} Anda"
            return
        }
        if (game.hasZoneId && zoneId.trim().isEmpty()) {
            _formError.value = "Masukkan ${game.zoneFieldLabel ?: "Zone ID"} Anda"
            return
        }
        if (pkg == null) {
            _formError.value = "Pilih nominal top-up"
            return
        }
        if (payment == null) {
            _formError.value = "Pilih metode pembayaran"
            return
        }

        val timestamp = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        val dateString = dateFormat.format(Date(timestamp))
        val randomDigits = (1000..9999).random()
        val invoice = "INV-$dateString-$randomDigits"
        
        val expiryTime = timestamp + (5 * 60 * 1000) // 5 minutes expiration

        val transaction = TopUpTransaction(
            gameName = game.name,
            userId = userId,
            zoneId = if (game.hasZoneId) zoneId else null,
            itemName = pkg.name,
            price = pkg.price + payment.fee,
            paymentMethod = payment.name,
            paymentStatus = "PENDING",
            transactionTime = timestamp,
            paymentExpiryTime = expiryTime,
            invoiceNumber = invoice
        )

        viewModelScope.launch {
            val insertedId = repository.insert(transaction)
            val createdTx = transaction.copy(id = insertedId.toInt())
            _activeTransaction.value = createdTx
            startPaymentTimer()
            onSuccess(insertedId.toInt())
        }
    }

    fun loadActiveTransaction(id: Int) {
        viewModelScope.launch {
            repository.getTransactionById(id).collect { tx ->
                if (tx != null) {
                    _activeTransaction.value = tx
                    // recalculate timer
                    val current = System.currentTimeMillis()
                    val remaining = ((tx.paymentExpiryTime - current) / 1000).toInt()
                    if (remaining > 0 && tx.paymentStatus == "PENDING") {
                        _paymentCountdown.value = remaining
                        startPaymentTimer()
                    } else {
                        _paymentCountdown.value = 0
                        if (tx.paymentStatus == "PENDING") {
                            repository.updateStatus(tx.id, "CANCELLED")
                        }
                    }
                }
            }
        }
    }

    private fun startPaymentTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_paymentCountdown.value > 0) {
                delay(1000)
                _paymentCountdown.value -= 1
            }
            // Timer expired, mark as cancelled if still pending
            val active = _activeTransaction.value
            if (active != null && active.paymentStatus == "PENDING") {
                repository.updateStatus(active.id, "CANCELLED")
                _activeTransaction.value = active.copy(paymentStatus = "CANCELLED")
            }
        }
    }

    fun simulatePaymentSuccess() {
        val active = _activeTransaction.value ?: return
        viewModelScope.launch {
            repository.updateStatus(active.id, "SUCCESS")
            _activeTransaction.value = active.copy(paymentStatus = "SUCCESS")
            timerJob?.cancel()
        }
    }

    fun cancelActiveTransaction() {
        val active = _activeTransaction.value ?: return
        viewModelScope.launch {
            repository.updateStatus(active.id, "CANCELLED")
            _activeTransaction.value = active.copy(paymentStatus = "CANCELLED")
            timerJob?.cancel()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
