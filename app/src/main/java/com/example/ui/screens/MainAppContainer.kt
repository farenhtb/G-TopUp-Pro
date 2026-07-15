package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameDataProvider
import com.example.ui.TopUpViewModel
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.SpaceCard
import com.example.ui.theme.SpaceDarkBg
import com.example.ui.theme.TextMuted

sealed class Screen {
    object Dashboard : Screen()
    data class GameDetail(val gameId: String) : Screen()
    data class PaymentProcessing(val transactionId: Int) : Screen()
}

@Composable
fun MainAppContainer(
    viewModel: TopUpViewModel,
    modifier: Modifier = Modifier
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Dashboard) }
    var currentTab by remember { mutableStateOf("HOME") } // "HOME", "HISTORY", "INFO"

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceDarkBg),
        containerColor = SpaceDarkBg,
        bottomBar = {
            // Only show Bottom Navigation Bar when on the main Dashboard screen stack
            if (currentScreen is Screen.Dashboard) {
                Surface(
                    color = SpaceCard,
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding(),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                ) {
                    NavigationBar(
                        containerColor = SpaceCard,
                        tonalElevation = 0.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        NavigationBarItem(
                            selected = currentTab == "HOME",
                            onClick = { currentTab = "HOME" },
                            icon = {
                                Icon(
                                    imageVector = if (currentTab == "HOME") Icons.Default.Home else Icons.Outlined.Home,
                                    contentDescription = "Beranda"
                                )
                            },
                            label = { Text("Beranda", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = CyberCyan,
                                selectedTextColor = CyberCyan,
                                indicatorColor = CyberPurple.copy(alpha = 0.3f),
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            ),
                            modifier = Modifier.testTag("nav_tab_home")
                        )

                        NavigationBarItem(
                            selected = currentTab == "HISTORY",
                            onClick = { currentTab = "HISTORY" },
                            icon = {
                                Icon(
                                    imageVector = if (currentTab == "HISTORY") Icons.Default.History else Icons.Outlined.History,
                                    contentDescription = "Riwayat"
                                )
                            },
                            label = { Text("Riwayat", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = CyberCyan,
                                selectedTextColor = CyberCyan,
                                indicatorColor = CyberPurple.copy(alpha = 0.3f),
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            ),
                            modifier = Modifier.testTag("nav_tab_history")
                        )

                        NavigationBarItem(
                            selected = currentTab == "INFO",
                            onClick = { currentTab = "INFO" },
                            icon = {
                                Icon(
                                    imageVector = if (currentTab == "INFO") Icons.Default.Info else Icons.Outlined.Info,
                                    contentDescription = "Bantuan"
                                )
                            },
                            label = { Text("Bantuan", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = CyberCyan,
                                selectedTextColor = CyberCyan,
                                indicatorColor = CyberPurple.copy(alpha = 0.3f),
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            ),
                            modifier = Modifier.testTag("nav_tab_info")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = currentScreen) {
                is Screen.Dashboard -> {
                    when (currentTab) {
                        "HOME" -> {
                            MainScreen(
                                viewModel = viewModel,
                                onGameSelect = { gameId ->
                                    val selected = GameDataProvider.games.find { it.id == gameId }
                                    if (selected != null) {
                                        viewModel.selectGame(selected)
                                        viewModel.clearForm()
                                        currentScreen = Screen.GameDetail(gameId)
                                    }
                                },
                                onTransactionSelect = { transactionId ->
                                    currentScreen = Screen.PaymentProcessing(transactionId)
                                }
                            )
                        }
                        "HISTORY" -> {
                            HistoryScreen(
                                viewModel = viewModel
                            )
                        }
                        "INFO" -> {
                            InfoScreen()
                        }
                    }
                }
                is Screen.GameDetail -> {
                    GameDetailScreen(
                        viewModel = viewModel,
                        onBackClick = {
                            currentScreen = Screen.Dashboard
                        },
                        onNavigateToPayment = { transactionId ->
                            currentScreen = Screen.PaymentProcessing(transactionId)
                        }
                    )
                }
                is Screen.PaymentProcessing -> {
                    PaymentProcessingScreen(
                        viewModel = viewModel,
                        transactionId = screen.transactionId,
                        onBackClick = {
                            // Back to dashboard
                            viewModel.clearForm()
                            currentScreen = Screen.Dashboard
                            // Force tab back to Home to check histories or topup again
                            currentTab = "HOME"
                        }
                    )
                }
            }
        }
    }
}
