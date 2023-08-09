package com.netdata.app.utils

import android.os.SystemClock
import android.text.format.DateUtils
import java.text.DecimalFormat
import java.util.*
import java.text.SimpleDateFormat

object AppUtils {
    private var mLastClickTime: Long = 0

    fun isOpenRecently(): Boolean {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 5000) {
            return true
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        return false
    }

    fun getTimeAgo(dateTime: String): String {
        val dateFormat = SimpleDateFormat(DateTimeFormats.SERVER_DATE_TIME_FORMAT_NEW, Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        try {
            val date = dateFormat.parse(dateTime)
            val timeInMillis = date.time
            return DateUtils.getRelativeTimeSpanString(timeInMillis, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "N/A"
    }

    fun convertTwoDecimal(value: Double, isPercent: Boolean = false): String {
        val decimalFormat = DecimalFormat("#.0")
        return if(isPercent){
            "${decimalFormat.format(value)}%"
        } else {
            decimalFormat.format(value)
        }
    }
}