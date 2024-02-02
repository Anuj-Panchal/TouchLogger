package com.example.touchlogger

import android.accessibilityservice.TouchInteractionController
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import com.example.touchlogger.csv.CsvRow
import com.example.touchlogger.csv.CsvUtil
import com.example.touchlogger.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var BATCH_SIZE: Int = 30;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val startButton = findViewById<Button>(R.id.start_button)

        startButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                setTouchEventListener();
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun setTouchEventListener() {
        val touchView = View(this)
        setContentView(touchView)

        touchView.setOnTouchListener { _, event ->
            handleTouchEvent(event)
            touchView.performClick()
        }
    }

    private fun handleTouchEvent(ev: MotionEvent): Boolean {
        val csvList: MutableList<CsvRow> = mutableListOf()
        val historySize: Int = ev.historySize;
        val pointerCount: Int = ev.pointerCount;
        for (h in 0 until historySize) {
            println("At time " + ev.getHistoricalEventTime(h));
            for (p in 0 until pointerCount) {
                println("  pointer"
                        + ev.getPointerId(p)
                        + ": (" + ev.getHistoricalX(p, h)
                        + "," + ev.getHistoricalY(p, h)
                        + "," + ev.getHistoricalPressure(p, h)
                        + "," + ev.getHistoricalSize(p, h)
                        + ")");
                csvList.add(
                    CsvRow(
                        ev.getPointerId(p).toString(),
                        ev.getHistoricalX(p, h).toDouble(),
                        ev.getHistoricalY(p, h).toDouble(),
                        ev.getHistoricalPressure(p, h).toDouble(),
                        ev.getHistoricalSize(p, h).toDouble(),
                        ev.getHistoricalEventTime(p)
                    )
                )
            }
        }
        println("At time " + ev.eventTime);
        for (p in 0 until pointerCount) {
            println("  pointer"
                    + ev.getPointerId(p)
                    + ": (" + ev.getX(p)
                    + "," + ev.getY(p)
                    + "," + ev.getPressure(p)
                    + "," + ev.getSize(p)
                    + ")");
            csvList.add(
                CsvRow(
                    ev.getPointerId(p).toString(),
                    ev.getX(p).toDouble(),
                    ev.getY(p).toDouble(),
                    ev.getPressure(p).toDouble(),
                    ev.getSize(p).toDouble(),
                    ev.eventTime
                )
            )
        }

        if(csvList.size >= BATCH_SIZE) {
            CsvUtil.writeCsv(this, "user_touch_logs" + System.nanoTime() + ".csv", csvList)
            csvList.clear()
        }
        return true;
    }
}