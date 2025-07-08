package com.example.lab6.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lab6.ui.screens.BingoScreen
import com.example.lab6.ui.screens.MainScreen
import com.example.lab6.ui.viewmodels.BingoViewModel

@Composable
fun BingoApp(context: Context) {
    val navController = rememberNavController()
    val viewModel = remember { BingoViewModel() }

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(
                onStartGame = { dimension, uid ->
                    viewModel.generateNewCard(dimension, uid)
                    navController.navigate("bingo")
                }
            )
        }
        composable("bingo") {
            BingoScreen(
                viewModel = viewModel,
                onRegenerate = { dimension, uid ->
                    viewModel.generateNewCard(dimension, uid)
                },
                onCheckBingo = viewModel::checkBingo,
                context = context
            )
        }
    }
}