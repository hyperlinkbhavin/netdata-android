package com.netdata.app.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object ConvertDateTimeFormat {
    fun dateTimeFormat(dateTime: String): String {
        val dateAndTime = dateTime.split("T")
        val date = dateAndTime[0]
        val getTime = dateAndTime[1].split(".")
        val time = getTime[0]
        val convertDate = convertDate(date, "yyyy-MM-dd", "MMM dd,yyyy", false)
        val convertTime = convertTo12Hours(time)

        return "$convertDate-$convertTime"
    }

    private fun convertTo12Hours(militaryTime: String): String {
        //in => "14:00:00"
        //out => "02:00 PM"
        val inputFormat = SimpleDateFormat("hh:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("hh:mm aa", Locale.getDefault())
        val date = inputFormat.parse(militaryTime)
        return outputFormat.format(date!!)
    }

    fun convertLocalToUtcDate(
        date: String,
        baseFormat: String,
        convertFormat: String
    ): String {
        try {
            // Convert to local
            val format =
                SimpleDateFormat(baseFormat, Locale.ENGLISH)
            format.timeZone = TimeZone.getDefault()
            val d = format.parse(date)

            // Convert to utc
            val serverFormat =
                SimpleDateFormat(convertFormat, Locale.ENGLISH)
            serverFormat.timeZone = TimeZone.getTimeZone("UTC")
            val finalDate = serverFormat.format(d!!)
            return finalDate.replace("AM", "am").replace("PM", "pm")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return date
    }

    fun convertDate(
        date: String,
        dtformat: String,
        needFormat: String,
        isSmallLetter: Boolean = false
    ): String {
        try {
            val format = SimpleDateFormat(dtformat, Locale.ENGLISH)
            val d = format.parse(date)
            val serverFormat =
                SimpleDateFormat(needFormat, Locale.ENGLISH)
            var finalDate = serverFormat.format(d!!)
            if (isSmallLetter) {
                finalDate = finalDate.replace("AM", "am").replace("PM", "pm")
            }
            return finalDate
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun convertUTCToLocalDate(
        date: String,
        baseFormat: String,
        convertFormat: String,
        isSmallLetter: Boolean = false
    ): String {
        try {
            // Convert to UTC
            val format =
                SimpleDateFormat(baseFormat, Locale.ENGLISH)
            format.timeZone = TimeZone.getTimeZone("UTC")
            val d = format.parse(date)
            // Convert to LOCAL
            val serverFormat =
                SimpleDateFormat(convertFormat, Locale.ENGLISH)
            serverFormat.timeZone = TimeZone.getDefault()
            var finalDate = serverFormat.format(d!!)
            if (isSmallLetter) {
                finalDate = finalDate.replace("AM", "am").replace("PM", "pm")
            }
            return finalDate
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return date
    }

    fun convertUTCToTimezone(
        date: String?,
        baseFormat: String,
        needFormat: String,
//        timezone: String?
    ): String {
        try {

            //            String fullDateString = DateTimeUtil.getFullDate(date, baseFormat); /*30-12-2018 08:00*/
/*01-01-1970 08:00*/ // sagar : 28/12/18 If the year date is less than 1990, it is not working here!
// sagar : 28/12/18 Otherwise, only above one line is enough instead of all string builder stuffs
            /** for timezone */
            val calendar = Calendar.getInstance(
                TimeZone.getTimeZone("GMT"),
                Locale.getDefault()
            )
            val currentLocalTime = calendar.time
            val date: DateFormat = SimpleDateFormat("Z")
            val timezone: String = date.format(currentLocalTime)
            /** ***** */


            val stringDate = StringBuilder()
            stringDate.append("30-12-2018 ") /*30-12-2018 */
            stringDate.append(date)
            val stringBaseFormat = StringBuilder()
            stringBaseFormat.append("dd-MM-yyyy ")
            stringBaseFormat.append(baseFormat)
            val sourceFormat =
                SimpleDateFormat(stringBaseFormat.toString(), Locale.ENGLISH)
            sourceFormat.timeZone = TimeZone.getTimeZone("UTC")
            val parsed = sourceFormat.parse(stringDate.toString())
            val tz = TimeZone.getTimeZone(timezone)
            val destFormat = SimpleDateFormat(needFormat, Locale.ENGLISH)
            destFormat.timeZone = tz
            return destFormat.format(parsed!!)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }

   /* @SuppressLint("SimpleDateFormat")
    fun formatToYesterdayOrToday(date: String?, context: Context): String? {
        val dateTime = SimpleDateFormat("yyyy-MM-dd").parse(date)
        val calendar = Calendar.getInstance()
        calendar.time = dateTime
        val today = Calendar.getInstance()
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DATE, -1)
        val timeFormatter: DateFormat = SimpleDateFormat("hh:mma")
        return if (calendar[Calendar.YEAR] === today[Calendar.YEAR] && calendar[Calendar.DAY_OF_YEAR] === today[Calendar.DAY_OF_YEAR]) {
            context.getString(R.string.label_today)
//            "Today " + timeFormatter.format(dateTime)
        } else if (calendar[Calendar.YEAR] === yesterday[Calendar.YEAR] && calendar[Calendar.DAY_OF_YEAR] === yesterday[Calendar.DAY_OF_YEAR]) {
            context.getString(R.string.label_yesterday)
//            "Yesterday " + timeFormatter.format(dateTime)
        } else {
            convertDate(date!!,DateTimeFormats.DTF_YYYY_DASH_MM_DASH_DD, DateTimeFormats.DTF_MMM_DD_YYYY,false)
        }
    }*/

    /*fun getPrettyTime(startDate: String, context: Context): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val currentDate = sdf.format(Date())

        val SECOND: Long = 1000
        val MINUTE = SECOND * 60
        val HOUR = MINUTE * 60
        val DAY = HOUR * 24
        val MONTH = DAY * 30
        val YEAR = MONTH * 12
        val WEEK = DAY * 7

        val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val bgn = fmt.parse(startDate)
        val end = fmt.parse(currentDate)
        val difference = end!!.time - bgn!!.time

        val year = difference / YEAR
        val month = difference / MONTH
        val day = difference / DAY
        val week = difference / WEEK
        val hour = difference / HOUR
        val minute = difference / MINUTE
        val second = difference / SECOND
        var time: String? = null

        when {
            year > 0 -> time =
                convertDate(startDate, "yyyy-MM-dd HH:mm:ss", "hh:mm a", true)

            month > 0 -> time =
                convertDate(startDate, "yyyy-MM-dd HH:mm:ss", "hh:mm a", true)

            week > 0 -> time =
                convertDate(startDate, "yyyy-MM-dd HH:mm:ss", "hh:mm a", true)

            day > 0 -> time =
                convertDate(startDate, "yyyy-MM-dd HH:mm:ss", "hh:mm a", true)

            hour > 0 -> time =
                "$hour " + if (hour > 1) context.getString(R.string.label_hours) else context.getString(
                    R.string.label_hour
                )
            minute > 0 -> time =
                "$minute " + if (minute > 1) context.getString(R.string.minute_ago) else context.getString(
                    R.string.minute_ago
                )
            second > 0 -> time = context.getString(R.string.label_now)
        }
        if (time != null)
            return time

        return ""
    }*/


}