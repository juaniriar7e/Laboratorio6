package com.example.lab6.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlin.random.Random

data class BingoState(
    val dimension: Int = 5,
    val playerId: String = "",
    val bingoCard: List<List<Int>> = emptyList(),
    val markedNumbers: Set<Int> = setOf(0),
    val isBingo: Boolean = false
)

class BingoViewModel : ViewModel() {
    val state = mutableStateOf(BingoState())

    fun generateNewCard(dimension: Int, playerId: String) {
        state.value = BingoState(
            dimension = dimension,
            playerId = playerId,
            bingoCard = generateBingoCard(dimension),
            markedNumbers = setOf(0)
        )
    }

    fun toggleNumber(number: Int) {
        if (number == 0) return // No permitir desmarcar el espacio libre

        val current = state.value.markedNumbers.toMutableSet()
        if (current.contains(number)) {
            current.remove(number)
        } else {
            current.add(number)
        }
        state.value = state.value.copy(markedNumbers = current)
    }

    fun checkBingo(): Boolean {
        val card = state.value.bingoCard
        val marked = state.value.markedNumbers
        val dim = state.value.dimension

        // Verificar filas
        for (row in card) {
            if (row.all { marked.contains(it) }) return true
        }

        // Verificar columnas
        for (j in 0 until dim) {
            var columnComplete = true
            for (i in 0 until dim) {
                if (!marked.contains(card[i][j])) {
                    columnComplete = false
                    break
                }
            }
            if (columnComplete) return true
        }

        // Verificar diagonales
        var diag1 = true
        var diag2 = true
        for (i in 0 until dim) {
            if (!marked.contains(card[i][i])) diag1 = false
            if (!marked.contains(card[i][dim - 1 - i])) diag2 = false
        }

        return diag1 || diag2
    }

    companion object {
        fun generateRandomUID(): String {
            val charPool = ('A'..'Z') + ('0'..'9')
            return (1..6).map { charPool.random() }.joinToString("")
        }

        fun generateBingoCard(dimension: Int): List<List<Int>> {
            val totalCells = dimension * dimension
            val numbers = (1..100).shuffled().take(totalCells - 1).toMutableList()
            val card = MutableList(dimension) { MutableList(dimension) { 0 } }

            var index = 0
            for (i in 0 until dimension) {
                for (j in 0 until dimension) {
                    if (i != dimension / 2 || j != dimension / 2 || dimension % 2 == 0) {
                        card[i][j] = numbers[index++]
                    }
                }
            }
            return card
        }
    }
}