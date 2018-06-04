package com.careem.tmdb.careemapp.common

import org.joda.time.DateTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun formatSimpleDateToString(dt: Date) : String {
    val simpleDateFormat  = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    return simpleDateFormat.format(dt)
}

fun formatStringToSimpleDate(dt: String) : Date? {
    val simpleDateFormat  = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    try {
        return simpleDateFormat.parse(dt)
    } catch (pe: ParseException) {
        return null
    }
}

fun giveStartOfADay(cal: Calendar) : Calendar {
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal;
}