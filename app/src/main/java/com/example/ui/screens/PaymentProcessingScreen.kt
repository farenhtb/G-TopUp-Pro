package com.example.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PaymentProcessingScreen(
    viewModel: TopUpViewModel,
    transactionId: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Collect active transaction state
    LaunchedEffect(transactionId) {
        viewModel.loadActiveTransaction(transactionId)
    }

    val activeTx by viewModel.activeTransaction.collectAsState()
    val countdown by viewModel.paymentCountdown.collectAsState()

    val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    formatter.maximumFractionDigits = 0

    val infiniteTransition = rememberInfiniteTransition(label = "pulsate")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    if (activeTx == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SpaceDarkBg),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Memuat Transaksi...", color = Color.White)
        }
        return
    }

    val tx = activeTx!!
    val priceFormatted = formatter.format(tx.price).replace("Rp", "Rp ")

    val minutes = countdown / 60
    val seconds = countdown % 60
    val timerString = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

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
                    text = "Gerbang Pembayaran Otomatis",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Check current transaction state
            when (tx.paymentStatus) {
                "PENDING" -> {
                    // 1. Expiry Timer Header Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = SpaceCard),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Selesaikan Pembayaran Sebelum",
                                    fontSize = 12.sp,
                                    color = TextMuted
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = timerString,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Black,
                                    color = StatusPending,
                                    modifier = Modifier.scale(pulseScale)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = CyberCyan,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Sistem memeriksa pembayaran Anda secara otomatis",
                                        fontSize = 10.sp,
                                        color = CyberCyan
                                    )
                                }
                            }
                        }
                    }

                    // 2. Main Payment Detail Details (QR code or Bank Creds)
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = SpaceCard),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = tx.paymentMethod,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Nominal Tagihan",
                                    fontSize = 11.sp,
                                    color = TextMuted,
                                    modifier = Modifier.padding(top = 12.dp)
                                )
                                Text(
                                    text = priceFormatted,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Black,
                                    color = GoldToken,
                                    modifier = Modifier.padding(top = 4.dp)
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                // Render QR Code if QRIS, else Bank VA Details
                                if (tx.paymentMethod.contains("QRIS", ignoreCase = true)) {
                                    Box(
                                        modifier = Modifier
                                            .size(200.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color.White)
                                            .padding(12.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        // Draw a clean, beautifully stylized cyber QR code
                                        Canvas(modifier = Modifier.fillMaxSize()) {
                                            val w = size.width
                                            val h = size.height

                                            // Draw corners blocks
                                            drawRect(color = Color.Black, size = Size(w * 0.28f, h * 0.28f), topLeft = Offset(0f, 0f))
                                            drawRect(color = Color.White, size = Size(w * 0.16f, h * 0.16f), topLeft = Offset(w * 0.06f, w * 0.06f))
                                            drawRect(color = Color.Black, size = Size(w * 0.08f, h * 0.08f), topLeft = Offset(w * 0.1f, w * 0.1f))

                                            drawRect(color = Color.Black, size = Size(w * 0.28f, h * 0.28f), topLeft = Offset(w * 0.72f, 0f))
                                            drawRect(color = Color.White, size = Size(w * 0.16f, h * 0.16f), topLeft = Offset(w * 0.78f, w * 0.06f))
                                            drawRect(color = Color.Black, size = Size(w * 0.08f, h * 0.08f), topLeft = Offset(w * 0.82f, w * 0.1f))

                                            drawRect(color = Color.Black, size = Size(w * 0.28f, h * 0.28f), topLeft = Offset(0f, h * 0.72f))
                                            drawRect(color = Color.White, size = Size(w * 0.16f, h * 0.16f), topLeft = Offset(w * 0.06f, h * 0.78f))
                                            drawRect(color = Color.Black, size = Size(w * 0.08f, h * 0.08f), topLeft = Offset(w * 0.1f, h * 0.82f))

                                            // Draw mock QR pixels
                                            val pixelGrid = listOf(
                                                Offset(w * 0.4f, h * 0.1f), Offset(w * 0.5f, h * 0.1f), Offset(w * 0.6f, h * 0.2f),
                                                Offset(w * 0.4f, h * 0.3f), Offset(w * 0.55f, h * 0.4f), Offset(w * 0.65f, h * 0.45f),
                                                Offset(w * 0.1f, h * 0.4f), Offset(w * 0.2f, h * 0.5f), Offset(w * 0.3f, h * 0.45f),
                                                Offset(w * 0.75f, h * 0.45f), Offset(w * 0.85f, h * 0.5f), Offset(w * 0.9f, h * 0.4f),
                                                Offset(w * 0.45f, h * 0.6f), Offset(w * 0.55f, h * 0.6f), Offset(w * 0.65f, h * 0.7f),
                                                Offset(w * 0.5f, h * 0.8f), Offset(w * 0.6f, h * 0.85f), Offset(w * 0.8f, h * 0.75f)
                                            )
                                            pixelGrid.forEach { offset ->
                                                drawRect(
                                                    color = Color.Black,
                                                    size = Size(w * 0.08f, h * 0.08f),
                                                    topLeft = offset
                                                )
                                            }
                                        }
                                        // Center logo decoration
                                        Surface(
                                            modifier = Modifier.size(36.dp),
                                            color = CyberPurple,
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Default.QrCode2,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "Scan QRIS di atas dengan aplikasi pembayaran Anda (GoPay, OVO, ShopeePay, Dana, dll.)",
                                        fontSize = 10.sp,
                                        color = TextMuted,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(horizontal = 24.dp)
                                    )
                                } else {
                                    // Virtual Account Details
                                    Text(
                                        text = "Nomor Virtual Account",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 4.dp, bottom = 12.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(SpaceCardLighter)
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "8830 1234 5678 9012",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = CyberCyan,
                                            letterSpacing = 1.sp
                                        )
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = "Copy",
                                            tint = TextMuted,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Text(
                                        text = "Petunjuk Transfer: Masukkan kode bank di atas pada menu transfer Virtual Account m-banking Anda. Transaksi otomatis terverifikasi setelah transfer berhasil.",
                                        fontSize = 10.sp,
                                        color = TextMuted,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(horizontal = 12.dp)
                                    )
                                }
                            }
                        }
                    }

                    // 3. User & Game Summary Details Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = SpaceCard),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Detail Pesanan",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                RowJustify("Invoice ID", tx.invoiceNumber)
                                RowJustify("Game", tx.gameName)
                                RowJustify("Paket", tx.itemName)
                                RowJustify("User ID", tx.userId)
                                if (tx.zoneId != null) {
                                    RowJustify("Zone ID", tx.zoneId!!)
                                }
                            }
                        }
                    }

                    // 4. AUTOMATIC SIMULATOR BUTTONS (Crucial for mockup)
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = CyberPurple.copy(alpha = 0.15f)),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, CyberPurple)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = null,
                                        tint = CyberCyan,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Simulator Sistem Otomatis (Hanya Demo)",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CyberCyan
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Button(
                                        onClick = { viewModel.simulatePaymentSuccess() },
                                        colors = ButtonDefaults.buttonColors(containerColor = StatusSuccess),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier
                                            .weight(1.0f)
                                            .testTag("btn_simulate_success")
                                    ) {
                                        Text(
                                            text = "Selesaikan Pembayaran",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    }
                                    OutlinedButton(
                                        onClick = { viewModel.cancelActiveTransaction() },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusCancelled),
                                        border = BorderStroke(1.dp, StatusCancelled),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier
                                            .weight(0.8f)
                                            .testTag("btn_simulate_cancel")
                                    ) {
                                        Text(
                                            text = "Batalkan",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                "SUCCESS" -> {
                    // Success digital receipt
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            colors = CardDefaults.cardColors(containerColor = SpaceCard),
                            shape = RoundedCornerShape(24.dp),
                            border = BorderStroke(1.5.dp, StatusSuccess)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Success",
                                    tint = StatusSuccess,
                                    modifier = Modifier.size(72.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Pembayaran Berhasil!",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                                Text(
                                    text = "Top-up telah dikirim secara otomatis ke akun Anda.",
                                    fontSize = 12.sp,
                                    color = TextMuted,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                                )

                                Spacer(modifier = Modifier.height(1.dp))
                                // Decorative line
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(SpaceCardLighter)
                                )
                                Spacer(modifier = Modifier.height(20.dp))

                                RowJustify("Invoice No.", tx.invoiceNumber)
                                RowJustify("Status Transaksi", "SUKSES", color = StatusSuccess)
                                RowJustify("Waktu Pembayaran", SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale.getDefault()).format(Date(tx.transactionTime)))
                                RowJustify("Nama Game", tx.gameName)
                                RowJustify("Item Pembelian", tx.itemName)
                                RowJustify("ID Akun Game", tx.userId)
                                if (tx.zoneId != null) {
                                    RowJustify("ID Server/Zone", tx.zoneId)
                                }
                                RowJustify("Metode Bayar", tx.paymentMethod)
                                RowJustify("Total Bayar", priceFormatted, color = GoldToken)

                                Spacer(modifier = Modifier.height(28.dp))

                                Button(
                                    onClick = onBackClick,
                                    colors = ButtonDefaults.buttonColors(containerColor = CyberPurple),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("btn_payment_home")
                                ) {
                                    Text(
                                        text = "Kembali ke Beranda",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
                else -> {
                    // Cancelled/Failed Transaction state
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            colors = CardDefaults.cardColors(containerColor = SpaceCard),
                            shape = RoundedCornerShape(24.dp),
                            border = BorderStroke(1.5.dp, StatusCancelled)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Cancelled",
                                    tint = StatusCancelled,
                                    modifier = Modifier.size(72.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Transaksi Dibatalkan",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                                Text(
                                    text = "Masa tunggu transaksi telah habis atau transaksi dibatalkan oleh pengguna.",
                                    fontSize = 12.sp,
                                    color = TextMuted,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                                )

                                RowJustify("Invoice No.", tx.invoiceNumber)
                                RowJustify("Status", "DIBATALKAN", color = StatusCancelled)
                                RowJustify("Game", tx.gameName)
                                RowJustify("Paket", tx.itemName)
                                RowJustify("Total Bayar", priceFormatted, color = GoldToken)

                                Spacer(modifier = Modifier.height(28.dp))

                                Button(
                                    onClick = onBackClick,
                                    colors = ButtonDefaults.buttonColors(containerColor = CyberPurple),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("btn_failed_home")
                                ) {
                                    Text(
                                        text = "Kembali ke Beranda",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
fun RowJustify(
    label: String,
    value: String,
    color: Color = Color.White
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = TextMuted
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1.0f, fill = false)
        )
    }
}
