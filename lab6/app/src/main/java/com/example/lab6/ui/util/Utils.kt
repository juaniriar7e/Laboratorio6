package com.example.lab6.ui.util

import kotlin.random.Random

fun generateBingoCard(dimension: Int): List<List<Int>> {
    val numbers = mutableSetOf<Int>()
    val card = mutableListOf<MutableList<Int>>()

    // Generar números únicos (1-99)
    while (numbers.size < dimension * dimension - 1) {
        numbers.add(Random.nextInt(1, 100))
    }

    val numberList = numbers.toList().shuffled()
    var index = 0

    for (i in 0 until dimension) {
        val row = mutableListOf<Int>()
        for (j in 0 until dimension) {
            // Espacio libre en el centro para dimensiones impares
            if (dimension % 2 == 1 && i == dimension / 2 && j == dimension / 2) {
                row.add(0)
            } else {
                row.add(numberList[index++])
            }
        }
        card.add(row)
    }

    return card
}