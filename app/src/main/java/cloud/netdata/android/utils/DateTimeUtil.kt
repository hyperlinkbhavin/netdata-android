package cloud.netdata.android.utils

import android.text.format.DateUtils
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtil {
    private const val MINUTE = "11"
    private const val HOUR = "22"
    private const val DAYS = "33"

    fun isTomorrow(d: Date): Boolean {
        return DateUtils.isToday(d.time - DateUtils.DAY_IN_MILLIS)
    }

    fun isYesterday(d: Date): Boolean {
        return DateUtils.isToday(d.time + DateUtils.DAY_IN_MILLIS)
    }

    fun isToday(d: Date): Boolean {
        return DateUtils.isToday(d.time)
    }

    fun isPastDate(date: Date): Boolean {
        return date.before(Date())
    }

    fun getCurrentDate(): String? {
        val calendar = Calendar.getInstance()
        val currentDate = calendar.time
        val simpleDateFormat =
            SimpleDateFormat(DateTimeFormats.SERVER_DATE_TIME_FORMAT, Locale.US)
        return simpleDateFormat.format(currentDate)
    }

    fun getTomorrowDate(): Date {
        val gc = GregorianCalendar()
        gc.add(Calendar.DATE, 1)
        return gc.time
    }

    fun getDate(date: String, format: String): Date? {
        val sdf = SimpleDateFormat(format, Locale.US)
        return try {
            sdf.parse(date)
        } catch (e: ParseException) {
            Date()
        }
    }

    fun getDate(format: String): String {
        val sdf =
            SimpleDateFormat(format, Locale.US)
        val date = Date()
        return sdf.format(date)
    }

    fun getDate(date: Date, requiredFormat: String): String {
        val sdf =
            SimpleDateFormat(requiredFormat, Locale.US)
        return sdf.format(date)
    }

    fun getRelativeTimeSpan(
        date: String,
        yourDateFormat: String
    ): String {
        val yourDate: Date? = getDate(date, yourDateFormat)
        val epochTime = yourDate?.time
        return DateUtils.getRelativeTimeSpanString(
            epochTime!!,
            System.currentTimeMillis(),
            DateUtils.SECOND_IN_MILLIS
        ).toString()
    }


    fun getRelativeTimeSpan(
        date: String,
        yourDateFormat: String,
        covertToLocal: Boolean
    ): String {
        val dateTime = if (covertToLocal) {
            ConvertDateTimeFormat.convertUTCToLocalDate(
                date,
                DateTimeFormats.SERVER_DATE_TIME_FORMAT,
                DateTimeFormats.SERVER_DATE_TIME_FORMAT
            )
        } else {
            date
        }
        val yourDate: Date? = getDate(dateTime, yourDateFormat)
        val epochTime = yourDate?.time
        return DateUtils.getRelativeTimeSpanString(
            epochTime!!,
            System.currentTimeMillis(),
            DateUtils.SECOND_IN_MILLIS
        ).toString()
    }

    fun getRelativeYear(
        date: String,
        yourDateFormat: String,
        covertToLocal: Boolean
    ): String {
        val dateTime = if (covertToLocal) {
            ConvertDateTimeFormat.convertUTCToLocalDate(
                date,
                DateTimeFormats.SERVER_DATE_TIME_FORMAT,
                DateTimeFormats.SERVER_DATE_TIME_FORMAT
            )
        } else {
            date
        }
        val yourDate: Date? = getDate(dateTime, yourDateFormat)
        val epochTime = yourDate?.time
        Log.d(
            "DateTimeUtil",
            "DateTimeUtil: getRelativeYearInMilliS: " + DateUtils.getRelativeTimeSpanString(
                epochTime!!,
                System.currentTimeMillis(),
                DateUtils.YEAR_IN_MILLIS
            ).toString()
        )
        Log.d(
            "DateTimeUtil",
            "DateTimeUtil: getRelativeShowYear: " + DateUtils.getRelativeTimeSpanString(
                epochTime,
                System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_YEAR.toLong()
            ).toString()
        )
        Log.d(
            "DateTimeUtil",
            "DateTimeUtil: getRelativeNoYear: " + DateUtils.getRelativeTimeSpanString(
                epochTime,
                System.currentTimeMillis(),
                DateUtils.FORMAT_NO_YEAR.toLong()
            ).toString()
        )
        return DateUtils.getRelativeTimeSpanString(
            epochTime,
            System.currentTimeMillis(),
            DateUtils.YEAR_IN_MILLIS
        ).toString()
    }

    fun getRelativeDays(
        date: String,
        yourDateFormat: String,
        covertToLocal: Boolean
    ): String {
        val dateTime = if (covertToLocal) {
            ConvertDateTimeFormat.convertUTCToLocalDate(
                date,
                DateTimeFormats.SERVER_DATE_TIME_FORMAT,
                DateTimeFormats.SERVER_DATE_TIME_FORMAT
            )
        } else {
            date
        }
        val yourDate: Date? = getDate(dateTime, yourDateFormat)
        val epochTime = yourDate?.time
        Log.d(
            "DateTimeUtil",
            "DateTimeUtil: getRelativeDayInMilliS: " + DateUtils.getRelativeTimeSpanString(
                epochTime!!,
                System.currentTimeMillis(),
                DateUtils.DAY_IN_MILLIS
            ).toString()
        )
        Log.d(
            "DateTimeUtil",
            "DateTimeUtil: getRelativeShowYear: " + DateUtils.getRelativeTimeSpanString(
                epochTime,
                System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_YEAR.toLong()
            ).toString()
        )
        Log.d(
            "DateTimeUtil",
            "DateTimeUtil: getRelativeNoYear: " + DateUtils.getRelativeTimeSpanString(
                epochTime,
                System.currentTimeMillis(),
                DateUtils.FORMAT_NO_YEAR.toLong()
            ).toString()
        )
        return DateUtils.getRelativeTimeSpanString(
            epochTime,
            System.currentTimeMillis(),
            DateUtils.DAY_IN_MILLIS
        ).toString()
    }

    /* public static String getAge(String strDate, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            Calendar dob = Calendar.getInstance();
            Calendar today = Calendar.getInstance();

            dob.setTime(date);

            */
/*double years = (double) date.get(Calendar.YEAR) - (double) startDate.get(Calendar.YEAR);*/ /*

            double age = ((double) today.get(Calendar.YEAR)) - ((double) dob.get(Calendar.YEAR));

            // Bhargav Savasani : 22/10/19  if part calculate month count if u remove this then show result is 1.2,2.4, and so on
            if (age < 1) {
                float days = dob.get(Calendar.DAY_OF_MONTH) - today.get(Calendar.DAY_OF_MONTH);
                d(TAG, "DateTimeUtil: getAge: days: " + days / 12);

                if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                    age--;
                }

                double ageInt = age;

                d(TAG, "DateTimeUtil: getAge: ageInt = " + ageInt);

                d(TAG, "DateTimeUtil: Period: " + Period.between(LocalDate.of(dob.get(Calendar.YEAR), dob.get(Calendar.MONTH), dob.get(Calendar.DAY_OF_MONTH)), LocalDate.now()).getYears());

                float chronoDays = ChronoUnit.DAYS.between(LocalDate.of(dob.get(Calendar.YEAR), dob.get(Calendar.MONTH), dob.get(Calendar.DAY_OF_MONTH)), LocalDate.now());

                d(TAG, "DateTimeUtil: chronoDays: " + chronoDays);

                double chronoDaysInYears = chronoDays / 365;

                d(TAG, "DateTimeUtil: chronoDaysInYear: " + chronoDaysInYears);

                return String.format(Locale.getDefault(), "%.1f", chronoDaysInYears);
            } else {
                return String.valueOf((int) age);
            }
        }
        return "";
    }*/
    fun getDateDifferenceInYearMonth(
        startDate: Date?,
        endDate: Date?
    ): String {
        val startCalendar: Calendar = GregorianCalendar()
        startCalendar.time = startDate!!
        val endCalendar: Calendar = GregorianCalendar()
        endCalendar.time = endDate!!
        val diffYear =
            endCalendar[Calendar.YEAR] - startCalendar[Calendar.YEAR]
        val diffMonth =
            diffYear * 12 + endCalendar[Calendar.MONTH] - startCalendar[Calendar.MONTH]
        return if (Math.abs(diffYear) < 1) {
            "0" + "." + Math.abs(diffMonth)
        } else {
            Math.abs(diffYear).toString()
        }
    }

    fun getDateDifference(
        firstDate: Date,
        secondDate: Date,
        type: String
    ): Double {
        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()
        cal1.time = firstDate
        cal2.time = secondDate
        // Get the represented date in milliseconds
        val millis1 = cal1.timeInMillis
        val millis2 = cal2.timeInMillis
        // Calculate difference in milliseconds
        val diff = millis2 - millis1
        // Calculate difference in seconds
        val diffSeconds = diff / 1000
        // Calculate difference in minutes
        val diffMinutes = diff / (60 * 1000)
        // Calculate difference in hours
        val diffHours = diff / (60 * 60 * 1000)
        // Calculate difference in days
        val diffDays = diff / (24 * 60 * 60 * 1000)
        println("In milliseconds: $diff milliseconds.")
        println("In seconds: $diffSeconds seconds.")
        println("In minutes: $diffMinutes minutes.")
        println("In hours: $diffHours hours.")
        println("In days: $diffDays days.")
        if (type.equals(MINUTE, ignoreCase = true)) {
            return diffMinutes.toString().toDouble()
        } else if (type.equals(HOUR, ignoreCase = true)) {
            return diffHours.toString().toDouble()
        } else if (type.equals(DAYS, ignoreCase = true)) {
            return diffDays.toString().toDouble()
        }
        return diffDays.toString().toDouble()
    }


    fun getDateDifferenceInDays(
        firstDate: Date,
        secondDate: Date
    ): Double {
        return getDateDifference(firstDate, secondDate, DAYS)
    }

    fun getDateDifferenceInHours(
        firstDate: Date,
        secondDate: Date
    ): Double {
        return getDateDifference(firstDate, secondDate, HOUR)
    }

    fun getDateDifferenceInMinutes(
        firstDate: Date,
        secondDate: Date
    ): Double {
        return getDateDifference(firstDate, secondDate, MINUTE)
    }
}