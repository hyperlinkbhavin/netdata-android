package cloud.netdata.android.utils

object DateTimeFormats {

    const val DISPLAY_MONTH_YEAR = "MMM ''yy" //Jan '18

    const val DISPLAY_TIME = "MMMM d, yyyy 'at' hh:mm a" //September 9, 2011 at 2:20 pm

    const val DT_MMM_DD_YYYY = "MMM dd, yyyy" //Nov 01, 1999

    const val DISPLAY_SIMPLE_DATE_MONTH = "dd MMM" //02 Jan

    const val DISPLAY_DATE_MONTH_TIME = "dd MMM hh:mm a" //02 Jan 10:00 am

    const val DISPLAY_DATE_MONTH_COMMA_TIME = "dd MMM, hh:mm a" //02 Jan, 10:00 am

    const val DISPLAY_DATE_MONTH_DASH_TIME = "dd MMM-hh:mm a" //02 Jan-10:00 am

    const val DISPLAY_SIMPLE_TIME = "hh:mm a" //02:59 PM

    const val DTF_MMM_DD_YYYY = "MMM dd, yyyy" //Jan 01, 2019

    const val DTF_YYYY_DASH_MM_DASH_DD = "yyyy-MM-dd" //Jan 01, 2019

    const val moreThanYear = "MMMM d, yyyy" //"September 9, 2011

    const val moreThanYearEvent = "EEE, MMM d, yyyy 'at' h:mma" //Tue, September 9, 2011

    const val moreThenSevenDays = "MMM d 'at' h:mma" //March 30 at 1:14 pm

    const val moreThenSevenDaysEvent = "EEE, MMM d 'at' h:mma" //Tue, March 30 at 1:14 pm

    const val moreThenOneDays = "EEEE 'at' h:mma" //Friday at 1:48am

    const val yesterday = "'Yesterday at' h:mma" //Yesterday at 1:28pm

    const val today = "'Today at' h:mma" //Yesterday at 1:28pm

    const val tomorrow = "'Tomorrow at' h:mma" //Tomorrow : Tomorrow at 5 PM

    const val sameWeek = "EEEE 'at' h:mma" //Fri at 6:05 PM

    const val sameYear = "MMM d 'at' h:mma" //Nov 25 at 6:05 PM

    const val sameYearEvent = "EEE, MMM d 'at' h:mma" //Wed, March 30 at 1:14 pm

    const val moreThenOneYear = "MMM d , yyyy 'at' h:mma" //Jan 12, 2017 at 6:05 PM

    const val moreThenOneYearEvent = "EEE, MMM d, yyyy 'at' h:mma" //Wed, September 9, 2011

    const val SERVER_TIME_FORMAT = "HH:mm"
    const val LOCAL_TIME_FORMAT = "hh:mm a"
    const val SERVER_DATE_FORMAT = "yyyy-MM-dd"
    const val SERVER_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
    const val SERVER_DATE_TIME_FORMAT_NEW = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"
    const val MAINTENANCE_MODE_DATE_FORMAT = "dd/MM/yy, HH:mm"
    const val LOCAL_LIST_FORMAT = "dd MMM yyyy hh:mm a"
    const val BOOKING_LIST_FORMAT = "MMM dd, yyyy" //Mar 19, 2020
    const val REVIEW_LIST_FORMAT = "dd MMM" //5 Dec

    const val SEARCH_HOTEL_CHECK_IN_OUT_FORMAT = "MMM dd '('EEE')'" //Jun 10 (Mon)
    const val SELECT_ROOM_CHECK_IN_OUT_FORMAT = "MMM dd" //Jun 10
    const val NOTIFICATION_LIST_FORMAT = "dd/MM/yyyy"

    const val SDF = "u-MM-dd HH:mm:ss"
    const val PROFILE_BIRTH_DATE_FORMAT = "MM/dd/yyyy"
    const val PROFILE_MMM_YYYY = "MMM yyyy"
    const val TODAY_DATE_TIME_FORMAT = "'" + "' 'at' h:mm a"
    const val TOMORROW_DATE_TIME_FORMAT = "'Tomorrow' 'at' h:mm a"
    const val YESTERDAY_DATE_TIME_FORMAT = "'Yesterday' 'at' h:mm a"
    const val DAY_7_DATE_TIME_FORMAT = "EEEE 'at' h:mm a"
    const val DAY_OTHER_DATE_TIME_FORMAT = "EEE, dd MMM 'at' h:mm a"
    const val YEAR_DATE_TIME_FORMAT = "EEE, dd MMM yyyy 'at' h:mm a"
    const val DATE_FORMAT = "dd/MM/yyyy HH:mm:ss"
}