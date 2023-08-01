package com.netdata.app.data.pojo.request

data class FilterList(var name: String, var otherName: String? = null, var count: String? = null, var icon: Int?= null, var isIcon: Boolean = false, var isSelected: Boolean = false)
