package com.example.chess.view

import androidx.activity.compose.LocalActivity
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
fun MenuScreen(onNavigateToGame: () -> Unit, onNavigateToSettings: () -> Unit) {
    val activity = LocalActivity.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Шахматы",
            fontSize = 40.sp,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        Button(
            onClick = onNavigateToGame,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(text = "New Game")
        }

        Button(
            onClick = onNavigateToSettings,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(text = "Settings")
        }

        Button(
            onClick = { activity?.finish() }
        ) {
            Text(text = "Exit")
        }
    }
}