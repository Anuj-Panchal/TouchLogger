package com.example.touchlogger.csv

data class CsvRow(
    val pointer_id: String,
    val x_coordinate: Double,
    val y_coordinate: Double,
    val touch_pressure: Double,
    val finger_size: Double,
    val timestamp: Long
)
