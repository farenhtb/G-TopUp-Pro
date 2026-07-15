package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = CyberPurple,
    secondary = CyberCyan,
    tertiary = CyberPink,
    background = SpaceDarkBg,
    surface = SpaceCard,
    surfaceVariant = SpaceCardLighter,
    onBackground = TextLight,
    onSurface = TextLight,
    onSurfaceVariant = TextMuted,
    error = StatusCancelled
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force dark theme for cohesive gaming aesthetic
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
