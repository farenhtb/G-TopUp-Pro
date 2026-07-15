package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberPink
import com.example.ui.theme.CyberPurple

@Composable
fun GameLogoIcon(
    iconName: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = when (iconName) {
                        "ml" -> listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364))
                        "ff" -> listOf(Color(0xFFE65100), Color(0xFFF57C00), Color(0xFFFFB74D))
                        "pubg" -> listOf(Color(0xFF1B5E20), Color(0xFF4CAF50), Color(0xFF81C784))
                        "hok" -> listOf(Color(0xFFB71C1C), Color(0xFFE53935), Color(0xFFFF8A80))
                        "dota" -> listOf(Color(0xFF212121), Color(0xFF424242), Color(0xFF1E1E1E))
                        else -> listOf(CyberPurple, CyberPink)
                    }
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            when (iconName) {
                "ml" -> {
                    // Mobile Legends: Golden Crest Shield
                    val path = Path().apply {
                        moveTo(w * 0.5f, h * 0.25f)
                        lineTo(w * 0.75f, h * 0.35f)
                        lineTo(w * 0.7f, h * 0.65f)
                        lineTo(w * 0.5f, h * 0.85f)
                        lineTo(w * 0.3f, h * 0.65f)
                        lineTo(w * 0.25f, h * 0.35f)
                        close()
                    }
                    drawPath(path, color = Color(0xFFFFD54F))
                    drawCircle(color = Color(0xFF00E5FF), radius = w * 0.1f, center = Offset(w * 0.5f, h * 0.52f))
                }
                "ff" -> {
                    // Free Fire: Winged Amber Flame
                    val path = Path().apply {
                        moveTo(w * 0.2f, h * 0.8f)
                        quadraticTo(w * 0.4f, h * 0.7f, w * 0.45f, h * 0.4f)
                        quadraticTo(w * 0.5f, h * 0.2f, w * 0.75f, h * 0.15f)
                        quadraticTo(w * 0.65f, h * 0.5f, w * 0.8f, h * 0.75f)
                        quadraticTo(w * 0.5f, h * 0.85f, w * 0.2f, h * 0.8f)
                    }
                    drawPath(path, color = Color(0xFFFF3D00))
                    
                    val pathInner = Path().apply {
                        moveTo(w * 0.35f, h * 0.75f)
                        quadraticTo(w * 0.45f, h * 0.68f, w * 0.48f, h * 0.5f)
                        quadraticTo(w * 0.52f, h * 0.35f, w * 0.68f, h * 0.3f)
                        quadraticTo(w * 0.60f, h * 0.55f, w * 0.7f, h * 0.72f)
                        quadraticTo(w * 0.5f, h * 0.78f, w * 0.35f, h * 0.75f)
                    }
                    drawPath(pathInner, color = Color(0xFFFFC107))
                }
                "pubg" -> {
                    // PUBG Mobile: Steel Helmet Shield
                    drawRect(
                        color = Color(0xFF263238),
                        size = Size(w * 0.5f, h * 0.45f),
                        topLeft = Offset(w * 0.25f, h * 0.32f)
                    )
                    drawRect(
                        color = Color(0xFFCFD8DC),
                        size = Size(w * 0.42f, h * 0.1f),
                        topLeft = Offset(w * 0.29f, h * 0.42f)
                    )
                    drawRect(
                        color = Color(0xFF37474F),
                        size = Size(w * 0.08f, h * 0.12f),
                        topLeft = Offset(w * 0.46f, h * 0.65f)
                    )
                }
                "hok" -> {
                    // Honor of Kings: Golden Red Crown
                    val path = Path().apply {
                        moveTo(w * 0.2f, h * 0.7f)
                        lineTo(w * 0.3f, h * 0.35f)
                        lineTo(w * 0.45f, h * 0.55f)
                        lineTo(w * 0.5f, h * 0.25f)
                        lineTo(w * 0.55f, h * 0.55f)
                        lineTo(w * 0.7f, h * 0.35f)
                        lineTo(w * 0.8f, h * 0.7f)
                        lineTo(w * 0.5f, h * 0.82f)
                        close()
                    }
                    drawPath(path, color = Color(0xFFFFEA00))
                }
                "dota" -> {
                    // Dota 2: Iconic Split Red Box
                    drawRect(
                        color = Color(0xFFD50000),
                        size = Size(w * 0.6f, h * 0.6f),
                        topLeft = Offset(w * 0.2f, h * 0.2f)
                    )
                    // Split line diagonal
                    drawLine(
                        color = Color(0xFF212121),
                        start = Offset(w * 0.15f, h * 0.85f),
                        end = Offset(w * 0.85f, h * 0.15f),
                        strokeWidth = w * 0.15f
                    )
                    // Top-right dot
                    drawCircle(
                        color = Color(0xFF212121),
                        radius = w * 0.08f,
                        center = Offset(w * 0.7f, h * 0.3f)
                    )
                    // Bottom-left dot
                    drawCircle(
                        color = Color(0xFF212121),
                        radius = w * 0.08f,
                        center = Offset(w * 0.3f, h * 0.7f)
                    )
                }
            }
        }

        // Add sleek centered overlay game acronym text so the user definitely identifies it
        Text(
            text = when (iconName) {
                "ml" -> "MLBB"
                "ff" -> "FF"
                "pubg" -> "PUBG"
                "hok" -> "HOK"
                "dota" -> "DOTA"
                else -> ""
            },
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.align(Alignment.BottomCenter),
            maxLines = 1
        )
    }
}
