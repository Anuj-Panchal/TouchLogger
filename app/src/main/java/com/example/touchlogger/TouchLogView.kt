package com.example.touchlogger

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.touchlogger.csv.CsvRow
import com.example.touchlogger.csv.CsvUtil.logToCsv

class TouchLogView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {


//    private var BATCH_SIZE: Int = 30;
//    private val csvList: MutableList<CsvRow> = mutableListOf()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

//    fun onScroll(
//        e1: MotionEvent?, e2: MotionEvent?, distanceX: Double, distanceY: Double
//    ): Boolean {
//        if (e1 != null) {
//            for (p in 0 until e1.pointerCount) {
//                logToCsv(
//                    CsvRow(
//                        "1" ,
//                        distanceX,
//                        distanceY,
//                        e1.getPressure(p).toDouble(),
//                        e1.getSize(p).toDouble(),
//                        e1.eventTime,
//                        2
//                    ),
//                    csvList,
//                    BATCH_SIZE,
//                    this
//                )
//            }
//        }
//        return true
//    }

}