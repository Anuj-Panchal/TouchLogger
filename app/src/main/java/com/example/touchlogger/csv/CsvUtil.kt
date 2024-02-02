package com.example.touchlogger.csv

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileWriter

object CsvUtil {
    fun writeCsv(context: Context, fileName: String, data: List<CsvRow>) {
        try {
            val file = File(getExternalStorageDirectory(context), fileName)
            val fileWriter = FileWriter(file)

            fileWriter.append("pointer_id,x_coordinate, y_coordinate, touch_pressure, finger_size, timestamp")
            fileWriter.append("\n")

            for (row in data) {
                fileWriter.append("${row.pointer_id},${row.x_coordinate},${row.y_coordinate}, ${row.touch_pressure}, ${row.finger_size}, ${row.timestamp}")
                fileWriter.append("\n")
            }

            fileWriter.close()

            context.sendBroadcast(android.content.Intent(android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, android.net.Uri.fromFile(file)))

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getExternalStorageDirectory(context: Context): File {
        val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "CsvFolder")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        return directory
    }
}