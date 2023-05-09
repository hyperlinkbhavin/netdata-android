package com.netdata.app.data.pojo.enumclass

enum class AlertStatus(val type: String) {
    CRITICAL("Critical"),
    WARNING("Warning"),
    CLEAR("Clear")
}