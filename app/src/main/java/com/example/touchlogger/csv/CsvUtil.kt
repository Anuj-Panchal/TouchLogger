package com.example.touchlogger.csv

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileWriter
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.*;


object CsvUtil {

//    fun saveCsvFile(fileName: String, data: List<CsvRow>) {
//        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
//            addCategory(Intent.CATEGORY_OPENABLE)
//            type = "text/csv"
//            putExtra(Intent.EXTRA_TITLE, fileName)
//        }
//        saveDocumentLauncher.launch(intent)
//    }

    fun writeCsv(context: Context, fileName: String, data: List<CsvRow>) {

        // Get the public external storage directory
        val storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)

        // Create a File object for the external storage directory and the file name
        val csvFile = File(storageDirectory, fileName)
        try {

            val fileWriter = FileWriter(csvFile)

            fileWriter.append("pointer_id,x_coordinate, y_coordinate, touch_pressure, finger_size, timestamp")
            fileWriter.append("\n")

            for (row in data) {
                fileWriter.append("${row.pointer_id},${row.x_coordinate},${row.y_coordinate}, ${row.touch_pressure}, ${row.finger_size}, ${row.timestamp}")
                fileWriter.append("\n")
            }

            fileWriter.close()

            context.sendBroadcast(android.content.Intent(android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, android.net.Uri.fromFile(csvFile)))
            println("CSV file '$fileName' written to external storage successfully.")

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getExternalStorageDirectory(context: Context): File {
        val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Touch Logger")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        return directory
    }
}