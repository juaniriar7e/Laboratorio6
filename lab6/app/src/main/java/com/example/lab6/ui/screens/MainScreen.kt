package com.example.lab6.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab6.ui.viewmodels.BingoViewModel

@Composable
fun MainScreen(onStartGame: (Int, String) -> Unit) {
    var dimension by remember { mutableStateOf("5") }
    val uid = remember { BingoViewModel.generateRandomUID() }

    // Filtra solo dígitos numéricos
    val onDimensionChange = { newValue: String ->
        dimension = newValue.filter { it.isDigit() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "GENERAL BINGO",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        Text(
            text = "Ingrese la dimensión de la matriz del Bingo",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = dimension,
            onValueChange = onDimensionChange,
            label = { Text("Ej: 5 (para 5x5)") },
            singleLine = true,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )

        Text(
            text = "ID de Jugador: $uid",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                dimension.toIntOrNull()?.takeIf { it > 0 }?.let {
                    onStartGame(it, uid)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar Juego")
        }
    }
}