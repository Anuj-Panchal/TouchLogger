package com.example.touchlogger.csv

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileWriter
import com.example.touchlogger.TouchLogView


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

            fileWriter.append("pointer_id,x_coordinate, y_coordinate, touch_pressure, finger_size, timestamp, type")
            fileWriter.append("\n")

            for (row in data) {
                fileWriter.append("${row.pointer_id},${row.x_coordinate},${row.y_coordinate}, ${row.touch_pressure}, ${row.finger_size}, ${row.timestamp}, ${row.type}")
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

    fun logToCsv(csvRow: CsvRow, csvList: MutableList<CsvRow>, BATCH_SIZE: Int, context: Context): Boolean {
        csvList.add(csvRow);
        if(csvList.size >= BATCH_SIZE) {
            println("csv write")
            val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 123

            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE
                )
                //CsvUtil.saveCsvFile("user_touch_logs" + System.nanoTime() + ".csv", csvList)
                writeCsv(context, "user_touch_scroll_logs" + System.nanoTime() + ".csv", csvList)
            } else {
                //CsvUtil.saveCsvFile("user_touch_logs" + System.nanoTime() + ".csv", csvList)
                writeCsv(context, "user_touch_scroll_logs" + System.nanoTime() + ".csv", csvList)
            }

            csvList.clear()
        }
        return true;
    }
}