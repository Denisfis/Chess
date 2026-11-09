package com.example.chess.controller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chess.ui.theme.ChessTheme
import com.example.chess.view.GameScreen
import com.example.chess.view.MenuScreen
import com.example.chess.view.SettingsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChessTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "menu") {
                    composable(route = "menu") {
                        MenuScreen(
                            onNavigateToGame = { navController.navigate("game") },
                            onNavigateToSettings = { navController.navigate("settings") }
                        )
                    }
                    composable(route = "settings") {
                        SettingsScreen(
                            onNavigateToGame = { navController.navigate("game") },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable(route = "game") {
                        GameScreen(
                            onNavigateBack = { navController.popBackStack("menu", inclusive = false) }
                        )
                    }
                }
            }
        }
    }
}