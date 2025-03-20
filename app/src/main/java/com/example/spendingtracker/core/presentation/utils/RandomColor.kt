package com.example.spendingtracker.core.presentation.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlin.random.Random

fun randomColor(minBrightness: Int = 90): Int {
    val random = Random.Default
    val red = random.nextInt(minBrightness, 256)
    val green = random.nextInt(minBrightness, 256)
    val blue = random.nextInt(minBrightness, 256)
    return Color(red, green, blue).copy(0.3f).toArgb()
}