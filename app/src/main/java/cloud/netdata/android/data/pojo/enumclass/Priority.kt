package cloud.netdata.android.data.pojo.enumclass

enum class Priority(val fullName: String, val shortName: String) {
    HIGH_PRIORITY("High Priority", "High"),
    MEDIUM_PRIORITY("Medium Priority", "Medium"),
    LOW_PRIORITY("Low Priority", "Low")
}