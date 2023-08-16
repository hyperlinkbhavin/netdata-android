package cloud.netdata.android.utils

import android.text.format.DateUtils
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

/**
 * DateTimeFormatter
 */
class DateTimeFormatter private constructor() {

    private var outTimeZone = TimeZone.getDefault()

    val date: Date?
        get() = Companion.date

    fun timeZoneToConvert(timezone: String?): DateTimeFormatter {
        outTimeZone = TimeZone.getTimeZone(timezone)
        return this
    }

    fun timeZoneToConvert(timezone: TimeZone): DateTimeFormatter {
        outTimeZone = timezone
        return this
    }

    fun formatDateToLocalTimeZone(format: String?): String {
        return SimpleDateFormat(format, Locale.US).format(Companion.date)
    }

    fun formatDateToTimeZone(format: String?): String {
        val utc = outTimeZone
        val formatter = SimpleDateFormat(format, Locale.US)
        formatter.timeZone = utc
        return formatter.format(Companion.date)
    }

    fun formatDateToCurrentTimeZone(format: String?): String {
        val formatter = SimpleDateFormat(format, Locale.US)
        formatter.timeZone = TimeZone.getDefault()
        return formatter.format(Companion.date)
    }

    fun formatDateToLocalTimeZoneDisplay(format: String?): String {
        /*if (Locale.getDefault().getLanguage().equals("ar")) {
            return new SimpleDateFormat(format, Locale.getDefault()).format(date);
        } else {*/
        return SimpleDateFormat(format, Locale.US).format(Companion.date)
        /*}*/
    }

    fun timeIn12Hoursformat(): String {
        val utc = outTimeZone
        val formatter = SimpleDateFormat("hh:mm a", Locale.US)
        formatter.timeZone = utc
        return formatter.format(Companion.date)
    }

    fun timeIn24HoursformatWithoutAmPm(): String {
        val utc = outTimeZone
        val formatter = SimpleDateFormat("HH:mm", Locale.US)
        formatter.timeZone = utc
        return formatter.format(Companion.date)
    }

    fun timeIn24Hoursformat(): String {
        val utc = outTimeZone
        val formatter = SimpleDateFormat("kk:mm", Locale.US)
        formatter.timeZone = utc
        return formatter.format(Companion.date)
    }


    companion object {
        private var date: Date? = null

        /**
         * @param date :- Date as String object , Please also provide inFormat if not in format of "yyyy-MM-dd hh:mm:ss"
         * @return class Object
         */
        fun date(date: String?, pattern: String?): DateTimeFormatter {
            val timeFormater: DateFormat = SimpleDateFormat(pattern, Locale.US)
            timeFormater.timeZone = TimeZone.getDefault()
            try {
                Companion.date = timeFormater.parse(date)
            } catch (e: ParseException) {
                Companion.date = null
            }
            return DateTimeFormatter()
        }

        fun dateUTC(date: String?, pattern: String?): DateTimeFormatter {
            val timeFormater: DateFormat = SimpleDateFormat(pattern, Locale.US)
            timeFormater.timeZone = TimeZone.getTimeZone("UTC")
            try {
                Companion.date = timeFormater.parse(date)
            } catch (e: ParseException) {
                Companion.date = null
            }
            return DateTimeFormatter()
        }

        /**
         * @param date :- Date as Date object
         * @return class Object
         */
        fun date(date: Date?): DateTimeFormatter {
            Companion.date = date
            return DateTimeFormatter()
        }

        fun dateAndTimeGet(convertDateTime: String, convertFormate: String?): String {
            var time = ""
            try {
                val now = System.currentTimeMillis()
                val dateFormat = SimpleDateFormat(convertFormate)
                val convertedDate = dateFormat.parse(convertDateTime)
                val relavetime1 = DateUtils.getRelativeTimeSpanString(
                    convertedDate.time,
                    now,
                    DateUtils.SECOND_IN_MILLIS
                )
                time = "" + relavetime1
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return time
        }

        fun dateTimeConvertLocalToLocal(
            timeConvert: String?,
            input: String?,
            output: String?,
        ): String {
            val timeFormater: DateFormat = SimpleDateFormat(input)
            try {
                val time: Date
                timeFormater.timeZone = TimeZone.getDefault()
                time = timeFormater.parse(timeConvert)
                val timeFormaterSecond: DateFormat = SimpleDateFormat(output)
                return timeFormaterSecond.format(time)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return ""
        }

        fun dateDiffInYearAndMonth(dob: Calendar, end: Calendar?): String {
            var end = end
            if (end == null) {
                end = Calendar.getInstance()
            }
            var monthsBetween = 0
            var dateDiff = end!![Calendar.DAY_OF_MONTH] - dob[Calendar.DAY_OF_MONTH]
            if (dateDiff < 0) {
                val borrrow = end.getActualMaximum(Calendar.DAY_OF_MONTH)
                dateDiff = end[Calendar.DAY_OF_MONTH] + borrrow - dob[Calendar.DAY_OF_MONTH]
                monthsBetween--
                if (dateDiff > 0) {
                    monthsBetween++
                }
            } else {
                monthsBetween++
            }
            monthsBetween += end[Calendar.MONTH] - dob[Calendar.MONTH]
            /*monthsBetween += (end.get(Calendar.YEAR) - dob.get(Calendar.YEAR)) * 12;*/
            val years = end[Calendar.YEAR] - dob[Calendar.YEAR]
            return "$years yrs $monthsBetween mos"
        }

        /**
         * Method to extract the user's age from the entered Date of Birth.
         *
         * @param dob String The user's date of birth.
         * @return ageS String The user's age in years based on the supplied DoB.
         */
        fun getAge(dob: Calendar): String {
            /*Calendar dob = Calendar.getInstance();
        dob.set(year, month, day);*/
            val today = Calendar.getInstance()
            var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
            if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
                age--
            }
            val ageInt = age
            return ageInt.toString()
        }

        fun getDiffInMinutes(start: Calendar, end: Calendar): Long {
            val diff: Long = end.timeInMillis - start.timeInMillis
            return TimeUnit.MILLISECONDS.toMinutes(diff)
        }

        fun getDiffInHours(start: Calendar, end: Calendar): Double {
            val minutes: Long = getDiffInMinutes(start, end)
            return minutes.toDouble() / 60L
        }

        fun getDiffInDays(start: Calendar, end: Calendar): Int {
            val difference = abs(start.time.time - end.time.time);
            val differenceDates = difference / (24 * 60 * 60 * 1000);
            val dayDifference = differenceDates.toInt()
            return dayDifference
        }

        @Throws(ParseException::class)
        fun formatToYesterdayOrToday(date: String?): String? {
            val utcToLocal = uTCToLocal(
                FORMAT_CHAT,
                FORMAT_yyyyMMdd_hh_mm_ss_a,
                date
            )
            val dateTime =
                SimpleDateFormat(FORMAT_yyyyMMdd_hh_mm_ss_a).parse(utcToLocal)
            val calendar = Calendar.getInstance()
            calendar.time = dateTime
            val today = Calendar.getInstance()
            val yesterday = Calendar.getInstance()
            yesterday.add(Calendar.DATE, -1)
            val timeFormatter: DateFormat = SimpleDateFormat(FORMAT_hh_mm_a)
            return if (calendar[Calendar.YEAR] === today[Calendar.YEAR] && calendar[Calendar.DAY_OF_YEAR] === today[Calendar.DAY_OF_YEAR]) {


                val dateFormat = SimpleDateFormat(FORMAT_yyyyMMdd_hh_mm_ss_a)
                val pasTime: Date = dateFormat.parse(utcToLocal)
                val nowTime = Date()
                val dateDiff = nowTime.time - pasTime.time
                val second: Long = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
                val minute: Long = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
                val hour: Long = TimeUnit.MILLISECONDS.toHours(dateDiff)
                val day: Long = TimeUnit.MILLISECONDS.toDays(dateDiff)
                if (second < 60) {
                    "Just Now"
                } else {
                    timeFormatter.format(pasTime)
                }

            } else if (calendar[Calendar.YEAR] === yesterday[Calendar.YEAR] && calendar[Calendar.DAY_OF_YEAR] === yesterday[Calendar.DAY_OF_YEAR]) {
                "Yesterday " /*+ timeFormatter.format(dateTime)*/
            } else {
                val timeFormatter: DateFormat = SimpleDateFormat(FORMAT_ddMMyyyy)
                timeFormatter.format(dateTime)
            }
        }

        fun messageTimeTodayOrDateWithTime(date: String?): String? {
            val utcToLocal = uTCToLocal(
                FORMAT_CHAT,
                FORMAT_yyyyMMdd_hh_mm_ss_a,
                date
            )
            val dateTime =
                SimpleDateFormat(FORMAT_yyyyMMdd_hh_mm_ss_a).parse(utcToLocal)
            val calendar = Calendar.getInstance()
            calendar.time = dateTime!!
            val today = Calendar.getInstance()
            val yesterday = Calendar.getInstance()
            yesterday.add(Calendar.DATE, -1)
            val timeFormatter: DateFormat = SimpleDateFormat(FORMAT_hh_mm_a)
            if (calendar[Calendar.YEAR] === today[Calendar.YEAR] && calendar[Calendar.DAY_OF_YEAR] === today[Calendar.DAY_OF_YEAR]) {
                return timeFormatter.format(dateTime)
            } else {
                val dateTimeFormatter: DateFormat = SimpleDateFormat(FORMAT_dd_MM_yyyy_hh_mm_a)
                return dateTimeFormatter.format(dateTime)
            }
        }

        fun messageTimeTodayOrTimeOnly(date: String?): String? {
            val utcToLocal = uTCToLocal(
                FORMAT_CHAT,
                FORMAT_yyyyMMdd_hh_mm_ss_a,
                date
            )
            val dateTime =
                SimpleDateFormat(FORMAT_yyyyMMdd_hh_mm_ss_a).parse(utcToLocal)
            val calendar = Calendar.getInstance()
            calendar.time = dateTime!!
            val today = Calendar.getInstance()
            val yesterday = Calendar.getInstance()
            yesterday.add(Calendar.DATE, -1)
            val timeFormatter: DateFormat = SimpleDateFormat(FORMAT_hh_mm_a)
//            if (calendar[Calendar.YEAR] === today[Calendar.YEAR] && calendar[Calendar.DAY_OF_YEAR] === today[Calendar.DAY_OF_YEAR]) {
//                return timeFormatter.format(dateTime)
//            }
            return timeFormatter.format(dateTime)

        }

        fun chatHeaderDate(date: String?): String? {
            val utcToLocal = uTCToLocal(
                FORMAT_CHAT,
                FORMAT_yyyyMMdd_hh_mm_ss_a,
                date
            )
            val dateTime =
                SimpleDateFormat(FORMAT_yyyyMMdd_hh_mm_ss_a).parse(utcToLocal)

            val timeFormatter: DateFormat = SimpleDateFormat(FORMAT_MMMM_d_yyyy)
            return timeFormatter.format(dateTime)
        }

        fun compareTwoDate(date: String?, previousDate: String?): Boolean {
            return chatHeaderDate(date) == chatHeaderDate(previousDate)
        }

        fun messageDateYesterdayOrToday(date: String?): String? {
            val utcToLocal = uTCToLocal(
                FORMAT_CHAT,
                FORMAT_yyyyMMdd_hh_mm_ss_a,
                date
            )
            val dateTime =
                SimpleDateFormat(FORMAT_yyyyMMdd_hh_mm_ss_a).parse(utcToLocal)
            val calendar = Calendar.getInstance()
            calendar.time = dateTime
            val today = Calendar.getInstance()
            val yesterday = Calendar.getInstance()
            yesterday.add(Calendar.DATE, -1)
            val timeFormatter: DateFormat = SimpleDateFormat(FORMAT_hh_mm_a)
            return if (calendar[Calendar.YEAR] === today[Calendar.YEAR] && calendar[Calendar.DAY_OF_YEAR] === today[Calendar.DAY_OF_YEAR]) {
                "Today"
            } else if (calendar[Calendar.YEAR] === yesterday[Calendar.YEAR] && calendar[Calendar.DAY_OF_YEAR] === yesterday[Calendar.DAY_OF_YEAR]) {
                "Yesterday " /*+ timeFormatter.format(dateTime)*/
            } else {
                val timeFormatter: DateFormat = SimpleDateFormat(FORMAT_MMMM_d_yyyy)
                timeFormatter.format(dateTime)
            }
        }

        fun uTCToLocal(
            dateFormatInPut: String?,
            dateFomratOutPut: String?,
            datesToConvert: String?
        ): String? {
            var dateToReturn = datesToConvert
            val sdf = SimpleDateFormat(dateFormatInPut)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            var gmt: Date? = null
            val sdfOutPutToSend = SimpleDateFormat(dateFomratOutPut)
            sdfOutPutToSend.timeZone = TimeZone.getDefault()
            try {
                gmt = sdf.parse(datesToConvert)
                dateToReturn = sdfOutPutToSend.format(gmt)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return dateToReturn
        }


        /*
         long difference = Math.abs(date1.getTime() - date2.getTime());
         long differenceDates = difference / (24 * 60 * 60 * 1000);
         String dayDifference = Long.toString(differenceDates);
         textView.setText("The difference between two dates is " + dayDifference + " days");
         */

        /*df.getCalendar()*/
        val currentUTCTime: String
            get() {
                val df: DateFormat =
                    SimpleDateFormat(DateTimeFormats.SERVER_DATE_TIME_FORMAT_NEW, Locale.US)
                df.timeZone = TimeZone.getTimeZone("UTC")
                return df.format(Date()) /*df.getCalendar()*/
            }

        val currentLocalDate: String
            get() {
                val df: DateFormat = SimpleDateFormat(FORMAT_yyyyMMdd, Locale.US)
                return df.format(Calendar.getInstance().time)
            }


        /**
         * DateFormats
         */
        const val FORMAT_NODE = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val FORMAT_CHAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val FORMAT_HHMMSS = "HH:mm:ss"
        const val FORMAT_HHMM = "HH:mm"
        const val FORMAT_hhmma = "hh:mm a"
        const val FORMAT_EEEddMMMyyyy = "EEE, dd MMM, yyyy"
        const val FORMAT_ddMMMyyyy = "dd MMM, yyyy"
        const val FORMAT_dd_MM_yyyy = "dd/MM/yyyy"
        const val FORMAT_dd_MM_yyyy_dash = "dd-MM-yyyy"
        const val FORMAT_dd_MMM_yyyy_dash = "dd-MMM-yyyy"
        const val FORMAT_yyyyMMdd = "yyyy-MM-dd"
        const val FORMAT_yyyyMMdd_HHmmss = "yyyy-MM-dd HH:mm:ss"
        const val FORMAT_yyyyMMdd_hh_mm_ss = "yyyy-MM-dd HH:mm:ss"
        const val FORMAT_yyyyMMdd_hh_mm_ss_a = "yyyy-MM-dd hh:mm:ss a"
        const val FORMAT_ddMMyyyy = "dd-MM-yyyy"
        const val FORMAT_MMMM_d_yyyy = "MMMM d, yyyy"
        const val FORMAT_dd_MM_yyyy_hh_mm_a = "dd-MM-yyyy hh:mm a"
        const val FORMAT_hh_mm_a = "hh:mm a"
        const val FORMAT_MMMMdd = "MMMM dd"
        const val FORMAT_MMMdd = "MMM dd"
        const val FORMAT_ddMMMM = "dd MMMM"
        const val FORMAT_dMMMM = "d MMMM"
        const val FORMAT_MMMddyyyy = "MMM dd, yyyy"
        const val FORMAT_dMMMMyyyy = "d MMMM, yyyy"

        // common app date/time display formats
        const val FORMAT_DISPLAY_DATE = FORMAT_dd_MM_yyyy
        const val FORMAT_DISPLAY_TIME = FORMAT_hhmma
        const val FORMAT_DISPLAY_DATE_TIME = "$FORMAT_DISPLAY_DATE $FORMAT_DISPLAY_TIME"
    }
}