package com.netdata.app.utils

import android.os.SystemClock

object AppUtils {
    private var mLastClickTime: Long = 0

    fun isOpenRecently(): Boolean {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 5000) {
            return true
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        return false
    }
}