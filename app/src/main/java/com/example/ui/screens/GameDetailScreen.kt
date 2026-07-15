package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameDataProvider
import com.example.model.GameInfo
import com.example.model.GamePackage
import com.example.model.PaymentMethod
import com.example.ui.TopUpViewModel
import com.example.ui.components.GameLogoIcon
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberPink
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.GoldToken
import com.example.ui.theme.SpaceCard
import com.example.ui.theme.SpaceCardLighter
import com.example.ui.theme.SpaceDarkBg
import com.example.ui.theme.TextMuted
import java.text.NumberFormat
import java.util.Locale

@Composable
fun GameDetailScreen(
    viewModel: TopUpViewModel,
    onBackClick: () -> Unit,
    onNavigateToPayment: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val game by viewModel.selectedGame.collectAsState()
    val selectedPkg by viewModel.selectedPackage.collectAsState()
    val selectedPayment by viewModel.selectedPaymentMethod.collectAsState()
    val userId by viewModel.userIdInput.collectAsState()
    val zoneId by viewModel.zoneIdInput.collectAsState()
    val errorMsg by viewModel.formError.collectAsState()

    val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    formatter.maximumFractionDigits = 0

    if (game == null) return

    val currentGame = game!!

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SpaceDarkBg,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Top Up ${currentGame.name}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        },
        bottomBar = {
            // Summary Bottom Bar if package is chosen
            if (selectedPkg != null) {
                val basePrice = selectedPkg!!.price
                val fee = selectedPayment?.fee ?: 0.0
                val totalPrice = basePrice + fee
                val totalFormatted = formatter.format(totalPrice).replace("Rp", "Rp ")

                Surface(
                    color = SpaceCard,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    tonalElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Total Pembayaran", fontSize = 11.sp, color = TextMuted)
                            Text(
                                text = totalFormatted,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = GoldToken
                            )
                            if (selectedPayment != null) {
                                Text(
                                    text = "Metode: ${selectedPayment!!.name}",
                                    fontSize = 11.sp,
                                    color = CyberCyan
                                )
                            }
                        }
                        Button(
                            onClick = {
                                viewModel.initiateTopUp { transactionId ->
                                    onNavigateToPayment(transactionId)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberPurple),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .height(48.dp)
                                .testTag("btn_pay_now")
                        ) {
                            Text(
                                text = "Bayar Sekarang",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Description Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SpaceCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        GameLogoIcon(
                            iconName = currentGame.iconName,
                            modifier = Modifier.size(72.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = currentGame.name,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                text = currentGame.category,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyberCyan
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Proses Otomatis & Instan 24 Jam",
                                fontSize = 11.sp,
                                color = Color.LightGray
                            )
                        }
                    }
                }
            }

            // Error Message Banner if form has error
            if (errorMsg != null) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CyberPink.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Error",
                                tint = CyberPink,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = errorMsg!!,
                                fontSize = 12.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Step 1: Account Information Input Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SpaceCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = null,
                                tint = CyberCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Langkah 1: Lengkapi Akun",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = userId,
                                onValueChange = { viewModel.updateUserId(it) },
                                label = { Text(currentGame.idFieldLabel, color = TextMuted) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CyberPurple,
                                    unfocusedBorderColor = SpaceCardLighter,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                modifier = Modifier
                                    .weight(1.0f)
                                    .testTag("input_user_id")
                            )

                            if (currentGame.hasZoneId) {
                                OutlinedTextField(
                                    value = zoneId,
                                    onValueChange = { viewModel.updateZoneId(it) },
                                    label = { Text(currentGame.zoneFieldLabel ?: "Zone ID", color = TextMuted) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = CyberPurple,
                                        unfocusedBorderColor = SpaceCardLighter,
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .weight(0.6f)
                                        .testTag("input_zone_id")
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Petunjuk: Masukkan detail user ID game Anda dengan benar agar transaksi dapat langsung diproses otomatis.",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }
                }
            }

            // Step 2: Package Selection Grid Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SpaceCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Layers,
                                contentDescription = null,
                                tint = CyberCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Langkah 2: Pilih Nominal Top Up",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))

                        // Grid of packages
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val chunked = currentGame.packages.chunked(2)
                            chunked.forEach { pair ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    pair.forEach { pkg ->
                                        PackageItemCard(
                                            pkg = pkg,
                                            isSelected = selectedPkg?.id == pkg.id,
                                            priceFormatted = formatter.format(pkg.price).replace("Rp", "Rp "),
                                            onClick = { viewModel.selectPackage(pkg) },
                                            modifier = Modifier.weight(1.0f)
                                        )
                                    }
                                    if (pair.size == 1) {
                                        Spacer(modifier = Modifier.weight(1.0f))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Step 3: Payment Method Selection Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SpaceCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Payment,
                                contentDescription = null,
                                tint = CyberCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Langkah 3: Pilih Pembayaran Otomatis",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))

                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            GameDataProvider.paymentMethods.forEach { payment ->
                                PaymentMethodRow(
                                    payment = payment,
                                    isSelected = selectedPayment?.id == payment.id,
                                    feeFormatted = if (payment.fee == 0.0) "Bebas Biaya" else "+ " + formatter.format(payment.fee).replace("Rp", "Rp "),
                                    onClick = { viewModel.selectPaymentMethod(payment) }
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun PackageItemCard(
    pkg: GamePackage,
    isSelected: Boolean,
    priceFormatted: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderStroke = if (isSelected) {
        BorderStroke(2.dp, CyberCyan)
    } else {
        BorderStroke(1.dp, SpaceCardLighter)
    }

    val background = if (isSelected) {
        SpaceCardLighter
    } else {
        SpaceCard
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .border(borderStroke, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(12.dp)
            .testTag("package_${pkg.id}")
    ) {
        Column {
            Text(
                text = pkg.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (isSelected) CyberCyan else Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = priceFormatted,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = GoldToken
            )
        }
    }
}

@Composable
fun PaymentMethodRow(
    payment: PaymentMethod,
    isSelected: Boolean,
    feeFormatted: String,
    onClick: () -> Unit
) {
    val borderStroke = if (isSelected) {
        BorderStroke(1.5.dp, CyberCyan)
    } else {
        BorderStroke(1.dp, SpaceCardLighter)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) SpaceCardLighter else SpaceCard)
            .border(borderStroke, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(12.dp)
            .testTag("payment_${payment.id}"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = CyberCyan,
                    unselectedColor = TextMuted
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = payment.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = payment.category,
                    fontSize = 10.sp,
                    color = TextMuted
                )
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = feeFormatted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (payment.fee == 0.0) CyberCyan else GoldToken
            )
            if (payment.isAutoInstant) {
                Surface(
                    color = CyberPurple.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "INSTAN",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyberCyan,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
