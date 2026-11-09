package com.example.chess.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(onNavigateToGame: () -> Unit, onNavigateBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Настройки",
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        Button(
            onClick = onNavigateToGame,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(text = "Start Game")
        }

        Button(
            onClick = onNavigateBack
        ) {
            Text(text = "Back to Menu")
        }
    }
}