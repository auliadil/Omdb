package com.muhammadauliaadil.project.omdb.view.listener

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class RecyclerItemClickListener(context: Context, val listener: OnItemClickListener) : RecyclerView.OnItemTouchListener{

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    var gestureDetector =  GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            return true
        }
    })

    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        val childView = view.findChildViewUnder(e.x, e.y)
        if (childView != null && listener != null && gestureDetector.onTouchEvent(e)) {
            listener!!.onItemClick(childView, view.getChildAdapterPosition(childView))
        }

        Log.i("INFO", e.toString())
        return false
    }

    override fun onTouchEvent(view: RecyclerView, motionEvent: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }
}