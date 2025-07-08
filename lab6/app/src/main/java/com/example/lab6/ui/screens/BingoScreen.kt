package com.example.lab6.ui.screens

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab6.ui.viewmodels.BingoViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BingoScreen(
    viewModel: BingoViewModel,
    onRegenerate: (Int, String) -> Unit,
    onCheckBingo: () -> Boolean,
    context: Context
) {
    val state = viewModel.state.value
    val showBingoDialog = remember { mutableStateOf(false) }
    val tts = remember { TextToSpeech(context, null) }

    // Efecto para verificar bingo cuando cambian los números marcados
    LaunchedEffect(state.markedNumbers) {
        if (onCheckBingo()) {
            showBingoDialog.value = true
            tts.speak("¡Bingo!", TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    // Mostrar alerta de bingo
    if (showBingoDialog.value) {
        AlertDialog(
            onDismissRequest = { showBingoDialog.value = false },
            title = { Text("¡BINGO!") },
            text = { Text("¡Felicidades, has ganado!") },
            confirmButton = {
                Button(onClick = {
                    showBingoDialog.value = false
                    tts.stop()
                }) {
                    Text("Aceptar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            // TopAppBar mejorada sin parámetro de subtítulo
            TopAppBar(
                title = {
                    Column {
                        Text("Juego de Bingo")
                        Text(
                            "Jugador: ${state.playerId}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                FloatingActionButton(
                    onClick = {
                        onRegenerate(state.dimension, state.playerId)
                    },
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Regenerar carta")
                }

                ExtendedFloatingActionButton(
                    onClick = {
                        if (onCheckBingo()) {
                            showBingoDialog.value = true
                        }
                    },
                    icon = {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Validar bingo"
                        )
                    },
                    text = { Text("Validar Bingo") }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Grid de bingo optimizado
            LazyVerticalGrid(
                columns = GridCells.Fixed(state.dimension),
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                items(state.bingoCard.flatten()) { number ->
                    BingoCell(
                        number = number,
                        isMarked = state.markedNumbers.contains(number),
                        onTap = { viewModel.toggleNumber(number) }
                    )
                }
            }
        }
    }
}

@Composable
fun BingoCell(number: Int, isMarked: Boolean, onTap: () -> Unit) {
    Card(
        onClick = onTap,
        modifier = Modifier
            .padding(4.dp)
            .aspectRatio(1f),
        colors = CardDefaults.cardColors(
            containerColor = if (isMarked) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (number == 0) "FREE" else number.toString(),
                fontSize = 20.sp
            )
        }
    }
}