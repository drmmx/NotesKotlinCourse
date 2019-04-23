package com.drmmx.notes.customviews

import android.graphics.Paint
import android.widget.TextView

fun TextView.setStrikeThrough() {
    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

fun TextView.removeSrtikeThrough() {
    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
}