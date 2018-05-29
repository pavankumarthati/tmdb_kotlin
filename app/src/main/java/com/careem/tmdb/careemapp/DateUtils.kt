package com.careem.tmdb.careemapp

import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

fun formatToSimpleDate(dt: Date) : String {
    val simpleDateFormat  = SimpleDateFormat("YYYY-MM-dd", Locale.US)
    return simpleDateFormat.format(dt)
}