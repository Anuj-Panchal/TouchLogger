package com.example.touchlogger

import android.os.Bundle
import android.text.BoringLayout
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
import android.widget.ScrollView
import com.example.touchlogger.csv.CsvRow
import com.example.touchlogger.csv.CsvUtil
import com.example.touchlogger.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var BATCH_SIZE: Int = 30;
    private val csvList: MutableList<CsvRow> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        //requestPermissions()

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
        val touchView = ScrollView(this)
        setContentView(touchView)
        var isScroll: MutableList<Boolean> = mutableListOf(false);

        touchView.setOnTouchListener { _, event ->
            handleEvent(event, isScroll)
        }
    }

    private fun handleEvent(ev: MotionEvent, isScroll: MutableList<Boolean>): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_UP -> {
                isScroll[0] = false;
                CsvUtil.logToCsv(
                    CsvRow(
                        ev.getPointerId(0).toString(),
                        ev.getX(0).toDouble(),
                        ev.getY(0).toDouble(),
                        ev.getPressure(0).toDouble(),
                        ev.getSize(0).toDouble(),
                        ev.eventTime,
                        1
                    ),
                    csvList,
                    BATCH_SIZE,
                    this
                )
            }
            MotionEvent.ACTION_DOWN -> {
                isScroll[0] = true;
                CsvUtil.logToCsv(
                    CsvRow(
                        ev.getPointerId(0).toString(),
                        ev.getX(0).toDouble(),
                        ev.getY(0).toDouble(),
                        ev.getPressure(0).toDouble(),
                        ev.getSize(0).toDouble(),
                        ev.eventTime,
                        1
                    ),
                    csvList,
                    BATCH_SIZE,
                    this
                )
            }
            MotionEvent.ACTION_MOVE -> {
                if(isScroll[0]) {
                    CsvUtil.logToCsv(
                        CsvRow(
                            ev.getPointerId(0).toString(),
                            ev.getX(0).toDouble(),
                            ev.getY(0).toDouble(),
                            ev.getPressure(0).toDouble(),
                            ev.getSize(0).toDouble(),
                            ev.eventTime,
                            2
                        ),
                        csvList,
                        BATCH_SIZE,
                        this
                    )
                }
            }
        }
        return true;
    }
}