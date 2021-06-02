package com.example.modifiedchallenge

import android.content.Context
import android.graphics.Point
import android.view.WindowManager

object Tools {
    fun getScreenDimenss(): Point {
        val wm = App.instance.getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = Point()
        wm.defaultDisplay.getSize(size)
        return size
    }
}