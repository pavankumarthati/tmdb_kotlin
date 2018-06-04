package com.careem.tmdb.careemapp.views

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

class RecyclerViewItemClickListener(private val context: Context, private val mHandler: ClickHandler) : RecyclerView.OnItemTouchListener {

    val mGestureDetector : GestureDetector by lazy {
        GestureDetector(context, object: GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                return true;
            }

        })
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent) : Boolean {
        val view = rv.findChildViewUnder(e.getX(), e.getY());
        if (view != null && mHandler != null &&
                mGestureDetector.onTouchEvent(e)) {
            val vh = rv.getChildViewHolder(view);
            val position = vh.getAdapterPosition();
            mHandler.onClick(view, position);
        }
        return false;
    }


    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) : Unit {

    }


    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) : Unit {

    }
}

interface ClickHandler {
    fun onClick(view: View, position: Int) : Unit
}