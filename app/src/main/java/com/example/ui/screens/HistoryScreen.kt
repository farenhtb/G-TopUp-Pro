package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.TopUpTransaction
import com.example.ui.TopUpViewModel
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberPink
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.GoldToken
import com.example.ui.theme.SpaceCard
import com.example.ui.theme.SpaceCardLighter
import com.example.ui.theme.SpaceDarkBg
import com.example.ui.theme.StatusCancelled
import com.example.ui.theme.StatusPending
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.TextMuted
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: TopUpViewModel,
    modifier: Modifier = Modifier
) {
    val transactions by viewModel.allTransactions.collectAsState()
    var selectedFilter by remember { mutableStateOf("ALL") } // "ALL", "SUCCESS", "PENDING", "CANCELLED"
    var selectedTxForReceipt by remember { mutableStateOf<TopUpTransaction?>(null) }

    val filteredTransactions = when (selectedFilter) {
        "SUCCESS" -> transactions.filter { it.paymentStatus == "SUCCESS" }
        "PENDING" -> transactions.filter { it.paymentStatus == "PENDING" }
        "CANCELLED" -> transactions.filter { it.paymentStatus == "CANCELLED" }
        else -> transactions
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceDarkBg)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Histori Transaksi",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Text(
            text = "Daftar lengkap riwayat pembelian diamond dan koin game Anda",
            fontSize = 12.sp,
            color = TextMuted,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Filters list Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filter",
                tint = CyberCyan,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            FilterChipItem(
                label = "Semua",
                isSelected = selectedFilter == "ALL",
                onClick = { selectedFilter = "ALL" },
                modifier = Modifier.testTag("filter_all")
            )
            FilterChipItem(
                label = "Sukses",
                isSelected = selectedFilter == "SUCCESS",
                onClick = { selectedFilter = "SUCCESS" },
                modifier = Modifier.testTag("filter_success")
            )
            FilterChipItem(
                label = "Menunggu",
                isSelected = selectedFilter == "PENDING",
                onClick = { selectedFilter = "PENDING" },
                modifier = Modifier.testTag("filter_pending")
            )
            FilterChipItem(
                label = "Batal",
                isSelected = selectedFilter == "CANCELLED",
                onClick = { selectedFilter = "CANCELLED" },
                modifier = Modifier.testTag("filter_cancelled")
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (filteredTransactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "No records",
                        tint = TextMuted,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Tidak ada transaksi",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Belum ada transaksi dengan filter ini.",
                        fontSize = 12.sp,
                        color = TextMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp, start = 32.dp, end = 32.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredTransactions) { tx ->
                    RecentTransactionItem(
                        transaction = tx,
                        onClick = { selectedTxForReceipt = tx }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }

    // Interactive custom popup Dialog for receipt details
    if (selectedTxForReceipt != null) {
        ReceiptDetailsDialog(
            transaction = selectedTxForReceipt!!,
            onDismiss = { selectedTxForReceipt = null }
        )
    }
}

@Composable
fun FilterChipItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        color = if (isSelected) CyberPurple else SpaceCard,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, if (isSelected) CyberCyan else SpaceCardLighter),
        modifier = modifier
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else TextMuted,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun ReceiptDetailsDialog(
    transaction: TopUpTransaction,
    onDismiss: () -> Unit
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    formatter.maximumFractionDigits = 0
    val priceFormatted = formatter.format(transaction.price).replace("Rp", "Rp ")

    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale("in", "ID"))
    val dateString = sdf.format(Date(transaction.transactionTime))

    val statusColor = when (transaction.paymentStatus) {
        "SUCCESS" -> StatusSuccess
        "PENDING" -> StatusPending
        else -> StatusCancelled
    }

    val statusLabel = when (transaction.paymentStatus) {
        "SUCCESS" -> "TRANSAKSI SUKSES"
        "PENDING" -> "MENUNGGU PEMBAYARAN"
        else -> "TRANSAKSI BATAL"
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("receipt_dialog"),
            colors = CardDefaults.cardColors(containerColor = SpaceCard),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, SpaceCardLighter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = null,
                            tint = CyberCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Kuitansi Pembelian",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Status Badge Banner
                Surface(
                    color = statusColor.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, statusColor),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = statusLabel,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = statusColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Receipt Breakdown Rows
                RowJustify("No. Invoice", transaction.invoiceNumber)
                RowJustify("Tanggal Pemesanan", dateString)
                RowJustify("Nama Game", transaction.gameName)
                RowJustify("Item Game", transaction.itemName)
                RowJustify("ID Akun Game", transaction.userId)
                if (transaction.zoneId != null) {
                    RowJustify("ID Server/Zone", transaction.zoneId)
                }
                RowJustify("Metode Pembayaran", transaction.paymentMethod)

                Spacer(modifier = Modifier.height(1.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(SpaceCardLighter)
                        .padding(vertical = 12.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                RowJustify("Total Bayar", priceFormatted, color = GoldToken)

                Spacer(modifier = Modifier.height(24.dp))

                // Helpful note
                Text(
                    text = "Layanan bantuan hubungi CS kami dengan melampirkan No. Invoice diatas.",
                    fontSize = 10.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
