package com.careem.tmdb.careemapp

import android.view.View

interface ClickHandler {
    fun onClick(view: View, position: Int) : Unit
}