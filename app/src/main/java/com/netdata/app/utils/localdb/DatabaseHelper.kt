package com.netdata.app.utils.localdb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.netdata.app.data.pojo.enumclass.Priority
import com.netdata.app.data.pojo.response.AppDetails
import com.netdata.app.data.pojo.response.HomeNotificationList
import com.netdata.app.data.pojo.response.NotificationPriorityList
import com.netdata.app.data.pojo.response.SpaceList
import com.netdata.app.utils.Constant
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "netdata.db"
        private const val DATABASE_VERSION = 1

        // Define your table names and column names here
        private const val TABLE_SPACE = "space"
        private const val TABLE_NOTIFICATION_PRIORITY = "notificationPriority"
        private const val TABLE_APP_DETAILS = "appDetails"
        private const val TABLE_FETCH_NOTIFICATIONS = "fetchNotifications"

        // Define column names for TABLE_SPACE
        private const val ID = "id"
        private const val SLUG = "slug"
        private const val NAME = "name"
        private const val DESCRIPTION = "description"
        private const val ICON_URL = "iconURL"
        private const val CREATED_AT = "createdAt"
        private const val PERMISSIONS = "permissions"
        private const val GOT_PLAN = "gotPlan"
        private const val PLAN_DEFINITION = "planDefinition"

        // Define column names for TABLE_NOTIFICATION_PRIORITY
        private const val NP_ID= "id"
        private const val NP_PRIORITY = "priority"
        private const val NP_IS_SOUND = "isSound"
        private const val NP_SOUND_NAME = "soundName"
        private const val NP_SOUND_URL = "soundUrl"
        private const val NP_IS_BANNER = "isBanner"
        private const val NP_IS_VIBRATION = "isVibration"

        // Define column names for TABLE_APP_DETAILS
        private const val AD_ID = "id"
        private const val AD_SI_COOKIE = "siCookie"
        private const val AD_SV_COOKIE = "svCookie"
        private const val AD_IS_LOGIN = "isLogin"
        private const val AD_IS_NOTIFICATION_PRIORITY_DATA = "isNotificationPriorityData"

        // Define column names for TABLE_FETCH_NOTIFICATIONS
        private const val FN_ID = "id"
        private const val FN_NODE_ID = "nodeId"
        private const val FN_HOSTNAME = "hostname"
        private const val FN_REACHABLE = "reachable"
        private const val FN_ROOM_ID = "roomId"
        private const val FN_ROOM_NAME = "roomName"
        private const val FN_ROOM_SLUG = "roomSlug"
        private const val FN_USER_ID = "userId"
        private const val FN_USER_NAME = "userName"
        private const val FN_USER_EMAIL = "userEmail"
        private const val FN_USER_MOBILE_APP_TOKEN = "userMobileAppToken"
        private const val FN_ALARM_LOG = "alarmLog"
        private const val FN_ALARM_NAME = "alarmName"
        private const val FN_ALARM_ROLE = "alarmRole"
        private const val FN_ALARM_WHEN = "alarmWhen"
        private const val FN_ALARM_CHART = "alarmChart"
        private const val FN_ALARM_FAMILY = "alarmFamily"
        private const val FN_ALARM_STATUS = "alarmStatus"
        private const val FN_ALARM_DETAILS = "alarmDetails"
        private const val FN_ALARM_DURATION = "alarmDuration"
        private const val FN_ALARM_CALC_EXPR = "alarmCalcExpr"
        private const val FN_ALARM_CONF_FILE = "alarmConfFile"
        private const val FN_ALARM_EDIT_LINE = "alarmEditLine"
        private const val FN_ALARM_RAISED_BY = "alarmRaisedBy"
        private const val FN_ALARM_PREV_STATUS = "alarmPrevStatus"
        private const val FN_ALARM_EDIT_COMMAND = "alarmEditCommand"
        private const val FN_ALARM_CHART_CONTEXT = "alarmChartContext"
        private const val FN_ALARM_TRANSACTION_ID = "alarmTransactionId"
        private const val FN_ALARM_WARNING_COUNT = "alarmWarningCount"
        private const val FN_ALARM_CLASSIFICATION = "alarmClassification"
        private const val FN_ALARM_CRITICAL_COUNT = "alarmCriticalCount"
        private const val FN_ALARM_VALUE_WITH_UNITS = "alarmValueWithUnits"
        private const val FN_ALARM_NON_CLEAR_DURATION = "alarmNonClearDuration"
        private const val FN_ROOMS = "rooms"
        private const val FN_SPACE_ID = "spaceId"
        private const val FN_SPACE_NAME = "spaceName"
        private const val FN_SPACE_SLUG = "spaceSlug"
        private const val FN_IS_READ = "isRead"
        private const val FN_CREATED_AT = "createdAt"
        private const val FN_PRIORITY = "priority"

    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableSpaceQuery =
            "CREATE TABLE $TABLE_SPACE ($ID TEXT, $SLUG TEXT, $NAME TEXT, $DESCRIPTION TEXT, $ICON_URL TEXT, " +
                    "$CREATED_AT TEXT, $PERMISSIONS TEXT, $GOT_PLAN TEXT, $PLAN_DEFINITION TEXT )"
        db.execSQL(createTableSpaceQuery)

        val createTableNotificationPriorityQuery =
            "CREATE TABLE $TABLE_NOTIFICATION_PRIORITY ($NP_ID INTEGER, $NP_PRIORITY TEXT, $NP_IS_SOUND INTEGER, $NP_SOUND_NAME TEXT, $NP_SOUND_URL TEXT, $NP_IS_BANNER INTEGER, $NP_IS_VIBRATION INTEGER )"
        db.execSQL(createTableNotificationPriorityQuery)

        val createTableAppDetailsQuery =
            "CREATE TABLE $TABLE_APP_DETAILS ($AD_ID INTEGER, $AD_SI_COOKIE TEXT, $AD_SV_COOKIE TEXT, $AD_IS_LOGIN INTEGER, $AD_IS_NOTIFICATION_PRIORITY_DATA INTEGER )"
        db.execSQL(createTableAppDetailsQuery)

        val createTableFetchNotificationsQuery =
            "CREATE TABLE $TABLE_FETCH_NOTIFICATIONS ($FN_ID LONG, $FN_NODE_ID TEXT, $FN_HOSTNAME TEXT, $FN_REACHABLE INTEGER, $FN_ROOM_ID TEXT, $FN_ROOM_NAME TEXT, $FN_ROOM_SLUG TEXT, $FN_USER_ID TEXT, $FN_USER_NAME TEXT, " +
                    "$FN_USER_EMAIL TEXT, $FN_USER_MOBILE_APP_TOKEN TEXT, $FN_ALARM_LOG TEXT, $FN_ALARM_NAME TEXT, " +
                    "$FN_ALARM_ROLE TEXT, $FN_ALARM_WHEN TEXT, $FN_ALARM_CHART TEXT, $FN_ALARM_FAMILY TEXT, " +
                    "$FN_ALARM_STATUS TEXT, $FN_ALARM_DETAILS TEXT, $FN_ALARM_DURATION LONG, $FN_ALARM_CALC_EXPR TEXT," +
                    "$FN_ALARM_CONF_FILE TEXT, $FN_ALARM_EDIT_LINE INTEGER, $FN_ALARM_RAISED_BY TEXT, $FN_ALARM_PREV_STATUS TEXT, " +
                    "$FN_ALARM_EDIT_COMMAND TEXT, $FN_ALARM_CHART_CONTEXT TEXT, $FN_ALARM_TRANSACTION_ID TEXT, $FN_ALARM_WARNING_COUNT INTEGER, " +
                    "$FN_ALARM_CLASSIFICATION TEXT, $FN_ALARM_CRITICAL_COUNT INTEGER, $FN_ALARM_VALUE_WITH_UNITS TEXT, " +
                    "$FN_ALARM_NON_CLEAR_DURATION LONG, $FN_ROOMS TEXT, $FN_SPACE_ID TEXT, $FN_SPACE_NAME TEXT, $FN_SPACE_SLUG TEXT, " +
                    "$FN_IS_READ INTEGER, $FN_CREATED_AT TEXT, $FN_PRIORITY TEXT )"
        db.execSQL(createTableFetchNotificationsQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Perform necessary migrations or updates here
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SPACE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTIFICATION_PRIORITY")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_APP_DETAILS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FETCH_NOTIFICATIONS")
        onCreate(db)
    }

    /**
     * Get All Table List
     */
    fun getTableList(){
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null)
        val tableNames = ArrayList<String>()

        while (cursor.moveToNext()) {
            val tableName = cursor.getString(0)
            tableNames.add(tableName)
        }

        cursor.close()
    }

    /**
     * App Data
     */
    fun insertAppData() {
        val values = ContentValues().apply {
            put(AD_ID, 1)
            put(AD_SI_COOKIE, "")
            put(AD_SV_COOKIE, "")
            put(AD_IS_LOGIN, 0)
            put(AD_IS_NOTIFICATION_PRIORITY_DATA, 0)
        }

        val db = writableDatabase
        db.insert(TABLE_APP_DETAILS, null, values)
        db.close()
    }

    fun updateAppData(siCookie: String? = null, svCookie: String? = null, isLogin: Int? = null, isNPTableData: Int? = null) {
        val values = ContentValues().apply {
            if(siCookie != null) put(AD_SI_COOKIE, siCookie)
            if(svCookie != null) put(AD_SV_COOKIE, svCookie)
            if(isLogin != null) put(AD_IS_LOGIN, isLogin)
            if(isNPTableData != null) put(AD_IS_NOTIFICATION_PRIORITY_DATA, isNPTableData)
        }

        val db = writableDatabase
        db.update(TABLE_NOTIFICATION_PRIORITY, values, "id = 1", null)
        db.close()
    }

    fun getAllAppData(): ArrayList<AppDetails> {
        val dataList = ArrayList<AppDetails>()
        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_APP_DETAILS"

        val cursor = db.rawQuery(selectQuery, null)
        cursor.use {
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
                val siCookie = cursor.getString(cursor.getColumnIndexOrThrow(ID))
                val svCookie = cursor.getString(cursor.getColumnIndexOrThrow(ID))
                val isLogin = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
                val isNPData = cursor.getInt(cursor.getColumnIndexOrThrow(ID))

                val data = AppDetails(id, siCookie, svCookie, isLogin, isNPData)
                dataList.add(data)
            }
        }

        db.close()
        Log.e("app data", dataList.toString())
        return dataList
    }

    /**
     * Space List Data
     */
    fun insertSpaceData(item: SpaceList) {
        val values = ContentValues().apply {
            put(ID, item.id)
            put(SLUG, item.slug)
            put(NAME, item.name)
            put(DESCRIPTION, item.description)
            put(ICON_URL, item.iconURL)
            put(CREATED_AT, item.createdAt)
            put(PERMISSIONS, Gson().toJson(item.permissions))
            put(GOT_PLAN, item.plan)
            put(PLAN_DEFINITION, Gson().toJson(item.planDefinition))
        }

        val db = writableDatabase
        db.insert(TABLE_SPACE, null, values)
        db.close()
    }

    fun getAllDataFromSpace(): ArrayList<SpaceList> {
        val dataList = ArrayList<SpaceList>()
        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_SPACE"

        val cursor = db.rawQuery(selectQuery, null)
        cursor.use {
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndexOrThrow(ID))
                val slug = cursor.getString(cursor.getColumnIndexOrThrow(SLUG))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(NAME))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION))
                val iconUrl = cursor.getString(cursor.getColumnIndexOrThrow(ICON_URL))
                val createdAt = cursor.getString(cursor.getColumnIndexOrThrow(CREATED_AT))
                val permissions = cursor.getString(cursor.getColumnIndexOrThrow(PERMISSIONS))
                val plan = cursor.getString(cursor.getColumnIndexOrThrow(GOT_PLAN))
                val planDefinition = cursor.getString(cursor.getColumnIndexOrThrow(PLAN_DEFINITION))
                val permissionsType = object : TypeToken<ArrayList<String>>() {}.type
                val permissionList: ArrayList<String> = Gson().fromJson(permissions, permissionsType)
                val data = SpaceList(id, slug, name, description, iconUrl, createdAt, permissionList, plan, Gson().fromJson(planDefinition, SpaceList.PlanDefinition::class.java))
                dataList.add(data)
            }
        }

        db.close()
        return dataList
    }

    /**
     * Notification Priority Data
     */
    fun insertNotificationPriorityData(item: NotificationPriorityList) {
        val values = ContentValues().apply {
            put(NP_ID, item.id)
            put(NP_PRIORITY, item.priority)
            put(NP_IS_SOUND, item.isSound)
            put(NP_SOUND_NAME, item.soundName)
            put(NP_IS_BANNER, item.isBanner)
            put(NP_IS_VIBRATION, item.isVibration)
        }

        val db = writableDatabase
        db.insert(TABLE_NOTIFICATION_PRIORITY, null, values)
        db.close()
    }

    fun updateNotificationPriorityData(isSound: Int? = null, soundName: String? = null, soundUrl: String? = null, isBanner: Int? = null, isVibration: Int? = null, conditionID: Int) {
        val values = ContentValues().apply {
            if(isSound != null) put(NP_IS_SOUND, isSound)
            if(soundName != null) put(NP_SOUND_NAME, soundName)
            if(soundUrl != null) put(NP_SOUND_URL, soundUrl)
            if(isBanner != null) put(NP_IS_BANNER, isBanner)
            if(isVibration != null) put(NP_IS_VIBRATION, isVibration)
        }

        val db = writableDatabase
        db.update(TABLE_NOTIFICATION_PRIORITY, values, "id = $conditionID", null)
        db.close()
    }

    fun getAllDataFromNotificationPriority(): ArrayList<NotificationPriorityList> {
        val dataList = ArrayList<NotificationPriorityList>()
        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NOTIFICATION_PRIORITY"

        val cursor = db.rawQuery(selectQuery, null)
        cursor.use {
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(NP_ID))
                val priority = cursor.getString(cursor.getColumnIndexOrThrow(NP_PRIORITY))
                val isSound = cursor.getInt(cursor.getColumnIndexOrThrow(NP_IS_SOUND))
                val soundName = cursor.getString(cursor.getColumnIndexOrThrow(NP_SOUND_NAME))
                val soundUrl = cursor.getString(cursor.getColumnIndexOrThrow(NP_SOUND_URL))
                val isBanner = cursor.getInt(cursor.getColumnIndexOrThrow(NP_IS_BANNER))
                val isVibration = cursor.getInt(cursor.getColumnIndexOrThrow(NP_IS_VIBRATION))
                val data = NotificationPriorityList(id, priority, isSound, soundName, soundUrl, isBanner, isVibration)
                dataList.add(data)
            }
        }

        db.close()
        Log.e("NP data", dataList.toString())
        return dataList
    }

    /**
     * Fetch Notification Data
     */
    fun insertFetchNotificationData(id: Long, item: HomeNotificationList) {
        val values = ContentValues().apply {
            put(FN_ID, id)
            put(FN_NODE_ID, item.data!!.node!!.id)
            put(FN_HOSTNAME, item.data!!.node!!.hostname)

            if(item.data!!.node!!.reachable!!){
                put(FN_REACHABLE, 1)
            } else {
                put(FN_REACHABLE, 0)
            }

            put(FN_ROOM_ID, item.data!!.room!!.id)
            put(FN_ROOM_NAME, item.data!!.room!!.name)
            put(FN_ROOM_SLUG, item.data!!.room!!.slug)
            put(FN_USER_ID, item.data!!.user!!.id)
            put(FN_USER_NAME, item.data!!.user!!.name)
            put(FN_USER_EMAIL, item.data!!.user!!.email)
            put(FN_USER_MOBILE_APP_TOKEN, item.data!!.user!!.MobileAppToken)
            put(FN_ALARM_LOG, Gson().toJson(item.data!!.alarm!!.log))
            put(FN_ALARM_NAME, item.data!!.alarm!!.name)
            put(FN_ALARM_ROLE, item.data!!.alarm!!.role)
            put(FN_ALARM_WHEN, item.data!!.alarm!!.whenData)
            put(FN_ALARM_CHART, item.data!!.alarm!!.chart)
            put(FN_ALARM_FAMILY, item.data!!.alarm!!.family)
            put(FN_ALARM_STATUS, item.data!!.alarm!!.status)
            put(FN_ALARM_DETAILS, item.data!!.alarm!!.details)
            put(FN_ALARM_DURATION, item.data!!.alarm!!.duration)
            put(FN_ALARM_CALC_EXPR, item.data!!.alarm!!.calcExpr)
            put(FN_ALARM_CONF_FILE, item.data!!.alarm!!.confFile)
            put(FN_ALARM_EDIT_LINE, item.data!!.alarm!!.editLine)
            put(FN_ALARM_RAISED_BY, item.data!!.alarm!!.raisedBy)
            put(FN_ALARM_PREV_STATUS, item.data!!.alarm!!.prevStatus)
            put(FN_ALARM_EDIT_COMMAND, item.data!!.alarm!!.editCommand)
            put(FN_ALARM_CHART_CONTEXT, item.data!!.alarm!!.chartContext)
            put(FN_ALARM_TRANSACTION_ID, item.data!!.alarm!!.transitionId)
            put(FN_ALARM_WARNING_COUNT, item.data!!.alarm!!.warningCount)
            put(FN_ALARM_CLASSIFICATION, item.data!!.alarm!!.classification)
            put(FN_ALARM_CRITICAL_COUNT, item.data!!.alarm!!.criticalCount)
            put(FN_ALARM_VALUE_WITH_UNITS, item.data!!.alarm!!.valueWithUnits)
            put(FN_ALARM_NON_CLEAR_DURATION, item.data!!.alarm!!.nonClearDuration)
            put(FN_ROOMS, Gson().toJson(item.data!!.rooms))
            put(FN_SPACE_ID, item.data!!.space!!.id)
            put(FN_SPACE_NAME, item.data!!.space!!.name)
            put(FN_SPACE_SLUG, item.data!!.space!!.slug)
            put(FN_IS_READ, 0)
            put(FN_CREATED_AT, item.createdAt)
            put(FN_PRIORITY, Priority.HIGH_PRIORITY.shortName)

            /*if (item.data!!.alarm!!.status.equals("critical", true)) {
                put(FN_PRIORITY, Priority.HIGH_PRIORITY.shortName)
            } else if (item.data!!.alarm!!.status.equals("warning", true)) {
                put(FN_PRIORITY, Priority.MEDIUM_PRIORITY.shortName)
            } else {
                put(FN_PRIORITY, Priority.LOW_PRIORITY.shortName)
            }*/
        }

        val db = writableDatabase
        db.insert(TABLE_FETCH_NOTIFICATIONS, null, values)
        db.close()
    }

    fun updateFetchNotificationData(item: HomeNotificationList) {
        val values = ContentValues().apply {
            put(FN_IS_READ, if (item.isRead) 1 else 0)
        }

        val db = writableDatabase
        db.update(TABLE_FETCH_NOTIFICATIONS, values, "id = ${item.id}", null)
        db.close()
    }

    fun updateFetchNotificationDataByAllRead() {
        val values = ContentValues().apply {
            put(FN_IS_READ, 1)
        }

        val db = writableDatabase
        db.update(TABLE_FETCH_NOTIFICATIONS, values, null, null)
        db.close()
    }

    fun updateFetchNotificationDataByNode(
        item: HomeNotificationList,
        isCurrentNode: Boolean = false
    ) {
        val values = ContentValues().apply {
            put(FN_PRIORITY, item.priority)
        }

        val db = writableDatabase
        if (isCurrentNode) {
            // Update the row where nodeId matches the specified value
            db.update(
                TABLE_FETCH_NOTIFICATIONS,
                values,
                "nodeId = '${item.data!!.node!!.id}'",
                null
            )
        } else {
            // Update all rows in the table
            db.update(TABLE_FETCH_NOTIFICATIONS, values, null, null)
        }
        db.close()
    }

    fun getLastIdFromTable(tableName: String): Long {
        val db = readableDatabase
        var lastId: Long = -1

        val query = "SELECT MAX(id) FROM $tableName"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            lastId = cursor.getLong(0)
        }

        cursor.close()
        db.close()

        return lastId
    }

    fun getAllDataFromFetchNotification(
        spaceID: String = "1", roomID: String = "1",
        isSortBy: Boolean = false, isFilterBy: Boolean = false,
        statusFilters: ArrayList<String> = ArrayList(),
        priorityFilters: ArrayList<String> = ArrayList(),
        nodesFilters: ArrayList<String> = ArrayList(),
        classFilters: ArrayList<String> = ArrayList(),
        typeFilters: ArrayList<String> = ArrayList(),
    ): ArrayList<HomeNotificationList> {
        val dataList = ArrayList<HomeNotificationList>()
        val db = readableDatabase

        var orderByQuery = ""
        var selectQuery = ""
        lateinit var cursor: Cursor
        val gson = Gson()

        if (isSortBy) {
            selectQuery = "SELECT * FROM $TABLE_FETCH_NOTIFICATIONS WHERE $FN_SPACE_ID = '$spaceID' AND $FN_ROOM_ID = '$roomID'"

            if (Constant.sortByCriticalityItemPosition != -1) {
                orderByQuery = """
                CASE $FN_ALARM_STATUS 
                WHEN 'critical' THEN 1 
                WHEN 'warning' THEN 2 
                WHEN 'clear' THEN 3 
                ELSE 4 
                END ${if (Constant.sortByCriticalityItemPosition == 0) "ASC" else "DESC"}
                """
            } else if (Constant.sortByNotificationPriorityItemPosition != -1) {
                orderByQuery += """
                CASE $FN_PRIORITY
                WHEN '${Priority.HIGH_PRIORITY.shortName}' THEN 1
                WHEN '${Priority.MEDIUM_PRIORITY.shortName}' THEN 2
                WHEN '${Priority.LOW_PRIORITY.shortName}' THEN 3
                ELSE 4
                END ${if (Constant.sortByNotificationPriorityItemPosition == 0) "ASC" else "DESC"}
        """
            } else if (Constant.sortByTimeItemPosition != -1) {
                orderByQuery += " $FN_CREATED_AT ${if (Constant.sortByTimeItemPosition == 1) "ASC" else "DESC"}"
            }

            if (orderByQuery.isNotEmpty()) {
                selectQuery += " ORDER BY $orderByQuery"
            }

            cursor = db.rawQuery(selectQuery, null)
        } else if (isFilterBy) {
            Log.e("status", "$statusFilters $priorityFilters")
            // Define the filter criteria (critical status or high priority)
            selectQuery = "SELECT * FROM $TABLE_FETCH_NOTIFICATIONS WHERE $FN_SPACE_ID = '$spaceID' AND $FN_ROOM_ID = '$roomID'"
            var statusArg = ""
            var priorityArg = ""
            var nodesArg = ""
            var classArg = ""
            var typeArg = ""

            if(statusFilters.isNotEmpty()){
                for(i in statusFilters){
                    statusArg += "$FN_ALARM_STATUS LIKE ('$i') OR "
                }
                statusArg = statusArg.dropLast(4)
            }
            if(priorityFilters.isNotEmpty()){
                for(i in priorityFilters){
                    priorityArg += "$FN_PRIORITY LIKE ('$i') OR "
                }
                priorityArg = priorityArg.dropLast(4)
            }
            if(nodesFilters.isNotEmpty()){
                for(i in nodesFilters){
                    nodesArg += "$FN_NODE_ID LIKE ('$i') OR "
                }
                nodesArg = nodesArg.dropLast(4)
            }
            if(classFilters.isNotEmpty()){
                for(i in classFilters){
                    classArg += "$FN_ALARM_CLASSIFICATION LIKE ('$i') OR "
                }
                classArg = classArg.dropLast(4)
            }
            if(typeFilters.isNotEmpty()){
                for(i in typeFilters){
                    typeArg += "$FN_ALARM_FAMILY LIKE ('$i') OR "
                }
                typeArg = typeArg.dropLast(4)
            }

            if(statusFilters.isNotEmpty()){
                selectQuery += " AND ($statusArg)"
            }
            if(priorityFilters.isNotEmpty()){
                selectQuery += " AND ($priorityArg)"
            }
            if(nodesFilters.isNotEmpty()){
                selectQuery += " AND ($nodesArg)"
            }
            if(classFilters.isNotEmpty()){
                selectQuery += " AND ($classArg)"
            }
            if(typeFilters.isNotEmpty()){
                selectQuery += " AND ($typeArg)"
            }

            selectQuery += " ORDER BY $FN_CREATED_AT DESC"

            Log.e("query", selectQuery)

            // Perform the query and get the cursor
            cursor = db.rawQuery(selectQuery, null)

        } else {
            selectQuery =
                "SELECT * FROM $TABLE_FETCH_NOTIFICATIONS WHERE $FN_SPACE_ID = '$spaceID' AND $FN_ROOM_ID = '$roomID' " +
                        "ORDER BY $FN_CREATED_AT DESC"
            cursor = db.rawQuery(selectQuery, null)
        }


        cursor.use {
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(FN_ID))
                val nodeId = cursor.getString(cursor.getColumnIndexOrThrow(FN_NODE_ID))
                val hostname = cursor.getString(cursor.getColumnIndexOrThrow(FN_HOSTNAME))
                val reachable = cursor.getInt(cursor.getColumnIndexOrThrow(FN_REACHABLE))
                val roomId = cursor.getString(cursor.getColumnIndexOrThrow(FN_ROOM_ID))
                val roomName = cursor.getString(cursor.getColumnIndexOrThrow(FN_ROOM_NAME))
                val roomSlug = cursor.getString(cursor.getColumnIndexOrThrow(FN_ROOM_SLUG))
                val userId = cursor.getString(cursor.getColumnIndexOrThrow(FN_USER_ID))
                val userName = cursor.getString(cursor.getColumnIndexOrThrow(FN_USER_NAME))
                val userEmail = cursor.getString(cursor.getColumnIndexOrThrow(FN_USER_EMAIL))
                val userMobileAppToken = cursor.getString(cursor.getColumnIndexOrThrow(FN_USER_MOBILE_APP_TOKEN))
                val alarmLog = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALARM_LOG))
                val alarmName = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALARM_NAME))
                val alarmRole = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALARM_ROLE))
                val alarmWhen = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALARM_WHEN))
                val alarmChart = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALARM_CHART))
                val alarmFamily = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALARM_FAMILY))
                val alarmStatus = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALARM_STATUS))
                val alarmDetails = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALARM_DETAILS))
                val alarmDuration = cursor.getLong(cursor.getColumnIndexOrThrow(FN_ALARM_DURATION))
                val alarmCalcExpr = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALARM_CALC_EXPR))
                val alarmConfFile = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALARM_CONF_FILE))
                val alarmEditLine = cursor.getLong(cursor.getColumnIndexOrThrow(FN_ALARM_EDIT_LINE))
                val alarmRaisedBy = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALARM_RAISED_BY))
                val alarmPrevStatus = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALARM_PREV_STATUS))
                val alarmEditCommand = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALARM_EDIT_COMMAND))
                val alarmChartContext = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALARM_CHART_CONTEXT))
                val alarmTransactionId = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALARM_TRANSACTION_ID))
                val alarmWarningCount = cursor.getLong(cursor.getColumnIndexOrThrow(FN_ALARM_WARNING_COUNT))
                val alarmClassification = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALARM_CLASSIFICATION))
                val alarmCriticalCount = cursor.getLong(cursor.getColumnIndexOrThrow(FN_ALARM_CRITICAL_COUNT))
                val alarmValueWithUnits = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALARM_VALUE_WITH_UNITS))
                val alarmNonClearDuration = cursor.getLong(cursor.getColumnIndexOrThrow(FN_ALARM_NON_CLEAR_DURATION))
                val rooms = cursor.getString(cursor.getColumnIndexOrThrow(FN_ROOMS))
                val spaceId = cursor.getString(cursor.getColumnIndexOrThrow(FN_SPACE_ID))
                val spaceName = cursor.getString(cursor.getColumnIndexOrThrow(FN_SPACE_NAME))
                val spaceSlug = cursor.getString(cursor.getColumnIndexOrThrow(FN_SPACE_SLUG))
                val isRead = cursor.getInt(cursor.getColumnIndexOrThrow(FN_IS_READ))
                val createdAt = cursor.getString(cursor.getColumnIndexOrThrow(FN_CREATED_AT))
                val priority = cursor.getString(cursor.getColumnIndexOrThrow(FN_PRIORITY))

                val allData = HomeNotificationList().data!!

                allData.node!!.id = nodeId
                allData.node!!.hostname = hostname
                allData.node!!.reachable = reachable == 1
                allData.room!!.id = roomId
                allData.room!!.name = roomName
                allData.room!!.slug = roomSlug
                allData.user!!.id = userId
                allData.user!!.name = userName
                allData.user!!.email = userEmail
                allData.user!!.MobileAppToken = userMobileAppToken

                val alarmLogType = object : TypeToken<ArrayList<HomeNotificationList.Data.Alarm.Log>>() {}.type
                val alarmLogList: ArrayList<HomeNotificationList.Data.Alarm.Log> = gson.fromJson(alarmLog, alarmLogType)
                allData.alarm!!.log = alarmLogList

                allData.alarm!!.name = alarmName
                allData.alarm!!.role = alarmRole
                allData.alarm!!.whenData = alarmWhen
                allData.alarm!!.chart = alarmChart
                allData.alarm!!.family = alarmFamily
                allData.alarm!!.status = alarmStatus
                allData.alarm!!.details = alarmDetails
                allData.alarm!!.duration = alarmDuration
                allData.alarm!!.calcExpr = alarmCalcExpr
                allData.alarm!!.confFile = alarmConfFile
                allData.alarm!!.editLine = alarmEditLine
                allData.alarm!!.raisedBy = alarmRaisedBy
                allData.alarm!!.prevStatus = alarmPrevStatus
                allData.alarm!!.editCommand = alarmEditCommand
                allData.alarm!!.chartContext = alarmChartContext
                allData.alarm!!.transitionId = alarmTransactionId
                allData.alarm!!.warningCount = alarmWarningCount
                allData.alarm!!.classification = alarmClassification
                allData.alarm!!.criticalCount = alarmCriticalCount
                allData.alarm!!.valueWithUnits = alarmValueWithUnits
                allData.alarm!!.nonClearDuration = alarmNonClearDuration

                val roomsType = object : TypeToken<ArrayList<HomeNotificationList.Data.Rooms>>() {}.type
                val roomsList: ArrayList<HomeNotificationList.Data.Rooms> = gson.fromJson(rooms, roomsType)
                allData.rooms = roomsList

                allData.space!!.id = spaceId
                allData.space!!.name = spaceName
                allData.space!!.slug = spaceSlug


                val data = HomeNotificationList(id, allData, createdAt, isRead == 1, priority)
                dataList.add(data)
            }
//            cursor.close()
        }

        db.close()
        return dataList
    }

    fun deleteFetchNotificationOlderThanWeek(date: String){
        val db = writableDatabase

        try {
            val deleteQuery = "DELETE FROM $TABLE_FETCH_NOTIFICATIONS WHERE $FN_CREATED_AT < ?;"
            db.execSQL(deleteQuery, arrayOf(date))

            val numRowsDeleted = db.compileStatement("SELECT changes()").simpleQueryForLong()
            Log.e("deleted", "$numRowsDeleted deleted")
        } catch (e: Exception) {
            Log.e("delete Error", "$e Error")
        } finally {
            db.close()
        }
    }

}