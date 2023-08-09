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
        private const val TABLE_ROOMS_DATA = "roomsData"

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
        private const val FN_HOST_ID = "hostId"
        private const val FN_HOST_NAME = "hostName"
        private const val FN_ALERT_NAME = "alertName"
        private const val FN_ALERT_ROLE = "alertRole"
        private const val FN_ALERT_CONFIG_HASH = "alertConfigHash"
        private const val FN_ALERT_CLASS = "alertClass"
        private const val FN_ALERT_TYPE = "alertType"
        private const val FN_ALERT_COMPONENT = "alertComponent"
        private const val FN_ALERT_CURRENT_STATUS = "alertCurrentStatus"
        private const val FN_ALERT_CURRENT_VALUE = "alertCurrentValue"
        private const val FN_ALERT_PREVIOUS_STATUS = "alertPreviousStatus"
        private const val FN_ALERT_PREVIOUS_VALUE = "alertPreviousValue"
        private const val FN_ALERT_TRANSITION_ID = "alertTransitionId"
        private const val FN_ALERT_ANNOTATION_INFO = "alertAnnotationInfo"
        private const val FN_ALERT_CONF_SOURCE = "alertConfSource"
        private const val FN_ALERT_CALC_EXPR = "alertCalcExpr"
        private const val FN_ALERT_EDIT_COMMAND = "alertEditCommand"
        private const val FN_ALERT_EDIT_LINE = "alertEditLine"
        private const val FN_ROOMS = "rooms"
        private const val FN_CHART_ID = "chartId"
        private const val FN_CHART_NAME = "chartName"
        private const val FN_SPACE_ID = "spaceId"
        private const val FN_SPACE_NAME = "spaceName"
        private const val FN_CONTEXT_NAME = "contextName"
        private const val FN_USER_ID = "userId"
        private const val FN_USER_NAME = "userName"
        private const val FN_USER_EMAIL = "userEmail"

        private const val FN_IS_READ = "isRead"
        private const val FN_CREATED_AT = "createdAt"
        private const val FN_TIMESTAMP = "timestamp"
        private const val FN_PRIORITY = "priority"

        // Define column names for TABLE_ROOMS_DATA
        private const val RD_ID = "id"
        private const val RD_NAME = "roomName"
        private const val RD_DATA_ID = "dataId"

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
            "CREATE TABLE $TABLE_FETCH_NOTIFICATIONS ($FN_ID LONG, $FN_HOST_ID TEXT, $FN_HOST_NAME TEXT, " +
                    "$FN_ALERT_NAME TEXT, $FN_ALERT_ROLE TEXT, $FN_ALERT_CONFIG_HASH TEXT, $FN_ALERT_CLASS TEXT, " +
                    "$FN_ALERT_TYPE TEXT, $FN_ALERT_COMPONENT TEXT, $FN_ALERT_CURRENT_STATUS TEXT, $FN_ALERT_CURRENT_VALUE DOUBLE, " +
                    "$FN_ALERT_PREVIOUS_STATUS TEXT, $FN_ALERT_PREVIOUS_VALUE DOUBLE, $FN_ALERT_TRANSITION_ID TEXT, " +
                    "$FN_ALERT_ANNOTATION_INFO TEXT, $FN_ALERT_CONF_SOURCE TEXT, $FN_ALERT_CALC_EXPR TEXT, " +
                    "$FN_ALERT_EDIT_COMMAND TEXT, $FN_ALERT_EDIT_LINE TEXT, $FN_ROOMS TEXT, $FN_CHART_ID TEXT, $FN_CHART_NAME TEXT, " +
                    "$FN_SPACE_ID TEXT, $FN_SPACE_NAME TEXT, $FN_CONTEXT_NAME TEXT, $FN_USER_ID TEXT, " +
                    "$FN_USER_NAME TEXT, $FN_USER_EMAIL TEXT, $FN_IS_READ INTEGER, $FN_CREATED_AT TEXT, " +
                    "$FN_TIMESTAMP TEXT, $FN_PRIORITY TEXT )"
        db.execSQL(createTableFetchNotificationsQuery)

        val createTableRoomsData =
            "CREATE TABLE $TABLE_ROOMS_DATA ($RD_ID TEXT, $RD_NAME TEXT, $RD_DATA_ID LONG )"
        db.execSQL(createTableRoomsData)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Perform necessary migrations or updates here
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SPACE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTIFICATION_PRIORITY")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_APP_DETAILS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FETCH_NOTIFICATIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ROOMS_DATA")
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
            put(FN_HOST_ID, item.data!!.host[0].id)
            put(FN_HOST_NAME, item.data!!.host[0].name)

            put(FN_ALERT_NAME, item.data!!.netdata!!.alert!!.name[0])
            put(FN_ALERT_ROLE, item.data!!.netdata!!.alert!!.role[0])
            put(FN_ALERT_CONFIG_HASH, item.data!!.netdata!!.alert!!.configHash)
            put(FN_ALERT_CLASS, item.data!!.netdata!!.alert!!.classes)
            put(FN_ALERT_TYPE, item.data!!.netdata!!.alert!!.type)
            put(FN_ALERT_COMPONENT, item.data!!.netdata!!.alert!!.component)
            put(FN_ALERT_CURRENT_STATUS, item.data!!.netdata!!.alert!!.current!!.status[0])
            put(FN_ALERT_CURRENT_VALUE, item.data!!.netdata!!.alert!!.current!!.value)
            put(FN_ALERT_PREVIOUS_STATUS, item.data!!.netdata!!.alert!!.previous!!.status)
            put(FN_ALERT_PREVIOUS_VALUE, item.data!!.netdata!!.alert!!.previous!!.value)
            put(FN_ALERT_TRANSITION_ID, item.data!!.netdata!!.alert!!.transition!!.id)
            put(FN_ALERT_ANNOTATION_INFO, item.data!!.netdata!!.alert!!.annotations!!.info)
            put(FN_ALERT_CONF_SOURCE, item.data!!.netdata!!.alert!!.annotations!!.confSource)
            put(FN_ALERT_CALC_EXPR, item.data!!.netdata!!.alert!!.annotations!!.calcExpr)
            put(FN_ALERT_EDIT_COMMAND, item.data!!.netdata!!.alert!!.annotations!!.editCommand)
            put(FN_ALERT_EDIT_LINE, item.data!!.netdata!!.alert!!.annotations!!.editLine)
            put(FN_ROOMS, Gson().toJson(item.data!!.netdata!!.room))
            put(FN_CHART_ID, item.data!!.netdata!!.chart!!.id)
            put(FN_CHART_NAME, item.data!!.netdata!!.chart!!.name)
            put(FN_SPACE_ID, item.data!!.netdata!!.space!!.id)
            put(FN_SPACE_NAME, item.data!!.netdata!!.space!!.name)
            put(FN_CONTEXT_NAME, item.data!!.netdata!!.context!!.name[0])
            put(FN_USER_ID, item.data!!.user!!.id)
            put(FN_USER_NAME, item.data!!.user!!.name)
            put(FN_USER_EMAIL, item.data!!.user!!.email)

            put(FN_IS_READ, 0)
            put(FN_CREATED_AT, item.createdAt)
            put(FN_TIMESTAMP, item.timestamp)
            put(FN_PRIORITY, Priority.HIGH_PRIORITY.shortName)
        }

        /*for(data in item.data!!.netdata!!.room){
            insertRoomsData(data, id)
        }*/

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
                "$FN_ID = '${item.id}'",
                null
            )
        } else {
            // Update all rows in the table
            db.update(
                TABLE_FETCH_NOTIFICATIONS,
                values,
                "nodeId = '${item.data!!.host[0].id}'",
                null
            )
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
        isSimpleData: Boolean = false,
        spaceID: String = "1", roomID: String = "1",
        isSortBy: Boolean = false, isFilterBy: Boolean = false,
        statusFilters: ArrayList<String> = ArrayList(),
        priorityFilters: ArrayList<String> = ArrayList(),
        nodesFilters: ArrayList<String> = ArrayList(),
        classFilters: ArrayList<String> = ArrayList(),
        typeFilters: ArrayList<String> = ArrayList(),
        sortByTimeItemPosition: Int = -1,
        sortByNotificationPriorityItemPosition: Int = -1,
        sortByCriticalityItemPosition: Int = -1
        ): ArrayList<HomeNotificationList> {
        val dataList = ArrayList<HomeNotificationList>()
        val db = readableDatabase

        var selectQuery = ""
        lateinit var cursor: Cursor
        val gson = Gson()
        selectQuery =
            "SELECT * FROM $TABLE_FETCH_NOTIFICATIONS WHERE $FN_SPACE_ID = '$spaceID'"
//            "SELECT * FROM $TABLE_FETCH_NOTIFICATIONS WHERE $FN_SPACE_ID = '$spaceID' AND $FN_ROOM_ID = '$roomID'"

        if (isSimpleData) {
            selectQuery =
                "SELECT * FROM $TABLE_FETCH_NOTIFICATIONS"
            cursor = db.rawQuery(selectQuery, null)
        } else if (isSortBy && isFilterBy) {
            selectQuery += " ${
                filterByQuery(
                    statusFilters = statusFilters,
                    priorityFilters = priorityFilters,
                    nodesFilters = nodesFilters,
                    classFilters = classFilters,
                    typeFilters = typeFilters
                )
            } ORDER BY ${sortByQuery(sortByTimeItemPosition, sortByNotificationPriorityItemPosition, sortByCriticalityItemPosition)}"

            cursor = db.rawQuery(selectQuery, null)
        } else if (isSortBy) {
//            selectQuery = "SELECT * FROM $TABLE_FETCH_NOTIFICATIONS WHERE $FN_SPACE_ID = '$spaceID' AND $FN_ROOM_ID = '$roomID'"

            if (sortByQuery(sortByTimeItemPosition, sortByNotificationPriorityItemPosition, sortByCriticalityItemPosition).isNotEmpty()) {
                selectQuery += " ORDER BY ${sortByQuery(sortByTimeItemPosition, sortByNotificationPriorityItemPosition, sortByCriticalityItemPosition)}"
            }

            cursor = db.rawQuery(selectQuery, null)
        } else if (isFilterBy) {
            // Define the filter criteria (critical status or high priority)
//            selectQuery = "SELECT * FROM $TABLE_FETCH_NOTIFICATIONS WHERE $FN_SPACE_ID = '$spaceID' AND $FN_ROOM_ID = '$roomID'"
            selectQuery += " ${
                filterByQuery(
                    statusFilters = statusFilters,
                    priorityFilters = priorityFilters,
                    nodesFilters = nodesFilters,
                    classFilters = classFilters,
                    typeFilters = typeFilters
                )
            } ORDER BY $FN_CREATED_AT DESC"

            // Perform the query and get the cursor
            cursor = db.rawQuery(selectQuery, null)

        } else {
            /*selectQuery =
                "SELECT * FROM $TABLE_FETCH_NOTIFICATIONS WHERE $FN_SPACE_ID = '$spaceID' AND $FN_ROOM_ID = '$roomID'"*/
            selectQuery += " ORDER BY $FN_CREATED_AT DESC"
            cursor = db.rawQuery(selectQuery, null)
        }

        Log.e("query", selectQuery)


        cursor.use {
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(FN_ID))
                val hostId = cursor.getString(cursor.getColumnIndexOrThrow(FN_HOST_ID))
                val hostName = cursor.getString(cursor.getColumnIndexOrThrow(FN_HOST_NAME))
                val alertName = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALERT_NAME))
                val alertRole = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALERT_ROLE))
                val alertConfigHash = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALERT_CONFIG_HASH))
                val alertClass = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALERT_CLASS))
                val alertType = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALERT_TYPE))
                val alertComponent = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALERT_COMPONENT))
                val alertCurrentStatus = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALERT_CURRENT_STATUS))
                val alertCurrentValue = cursor.getDouble(cursor.getColumnIndexOrThrow(FN_ALERT_CURRENT_VALUE))
                val alertPreviousStatus = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALERT_PREVIOUS_STATUS))
                val alertPreviousValue = cursor.getDouble(cursor.getColumnIndexOrThrow(FN_ALERT_PREVIOUS_VALUE))
                val alertTransitionId = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALERT_TRANSITION_ID))
                val alertAnnotationInfo = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALERT_ANNOTATION_INFO))
                val alertConfSource = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALERT_CONF_SOURCE))
                val alertCalcExpr = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALERT_CALC_EXPR))
                val alertEditCommand = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALERT_EDIT_COMMAND))
                val alertEditLine = cursor.getString(cursor.getColumnIndexOrThrow(FN_ALERT_EDIT_LINE))
                val rooms = cursor.getString(cursor.getColumnIndexOrThrow(FN_ROOMS))
                val chartId = cursor.getString(cursor.getColumnIndexOrThrow(FN_CHART_ID))
                val chartName = cursor.getString(cursor.getColumnIndexOrThrow(FN_CHART_NAME))
                val spaceId = cursor.getString(cursor.getColumnIndexOrThrow(FN_SPACE_ID))
                val spaceName = cursor.getString(cursor.getColumnIndexOrThrow(FN_SPACE_NAME))
                val contextName = cursor.getString(cursor.getColumnIndexOrThrow(FN_CONTEXT_NAME))
                val userId = cursor.getString(cursor.getColumnIndexOrThrow(FN_USER_ID))
                val userName = cursor.getString(cursor.getColumnIndexOrThrow(FN_USER_NAME))
                val userEmail = cursor.getString(cursor.getColumnIndexOrThrow(FN_USER_EMAIL))

                val isRead = cursor.getInt(cursor.getColumnIndexOrThrow(FN_IS_READ))
                val createdAt = cursor.getString(cursor.getColumnIndexOrThrow(FN_CREATED_AT))
                val timestamp = cursor.getString(cursor.getColumnIndexOrThrow(FN_TIMESTAMP))
                val priority = cursor.getString(cursor.getColumnIndexOrThrow(FN_PRIORITY))

                val allData = HomeNotificationList().data

                allData!!.host.add(HomeNotificationList.Data.Host(hostId,hostName))
                allData.netdata!!.alert!!.name.add(alertName)
                allData.netdata!!.alert!!.role.add(alertRole)
                allData.netdata!!.alert!!.configHash = alertConfigHash
                allData.netdata!!.alert!!.classes = alertClass
                allData.netdata!!.alert!!.type = alertType
                allData.netdata!!.alert!!.component = alertComponent
                allData.netdata!!.alert!!.current!!.status.add(alertCurrentStatus)
                allData.netdata!!.alert!!.current!!.value = alertCurrentValue
                allData.netdata!!.alert!!.previous!!.status = alertPreviousStatus
                allData.netdata!!.alert!!.previous!!.value = alertPreviousValue
                allData.netdata!!.alert!!.transition!!.id = alertTransitionId
                allData.netdata!!.alert!!.annotations!!.info = alertAnnotationInfo
                allData.netdata!!.alert!!.annotations!!.confSource = alertConfSource
                allData.netdata!!.alert!!.annotations!!.calcExpr = alertCalcExpr
                allData.netdata!!.alert!!.annotations!!.editCommand = alertEditCommand
                allData.netdata!!.alert!!.annotations!!.editLine = alertEditLine
                allData.netdata!!.chart!!.id = chartId
                allData.netdata!!.chart!!.name = chartName
                allData.netdata!!.space!!.id = spaceId
                allData.netdata!!.space!!.name = spaceName
                allData.netdata!!.context!!.name.add(contextName)
                allData.user!!.id = userId
                allData.user!!.name = userName
                allData.user!!.email = userEmail

                val roomsType = object : TypeToken<ArrayList<HomeNotificationList.Data.Netdata.Room>>() {}.type
                val roomsList: ArrayList<HomeNotificationList.Data.Netdata.Room> = gson.fromJson(rooms, roomsType)
                allData.netdata!!.room = roomsList
//                allData.netdata!!.room.addAll(getRoomsData(id))

                /*val alarmLogType = object : TypeToken<ArrayList<HomeNotificationList.Data.Alarm.Log>>() {}.type
                val alarmLogList: ArrayList<HomeNotificationList.Data.Alarm.Log> = gson.fromJson(alarmLog, alarmLogType)
                allData.alarm!!.log = alarmLogList


                val roomsType = object : TypeToken<ArrayList<HomeNotificationList.Data.Rooms>>() {}.type
                val roomsList: ArrayList<HomeNotificationList.Data.Rooms> = gson.fromJson(rooms, roomsType)
                allData.rooms = roomsList*/

                val data = HomeNotificationList(id, allData, createdAt, timestamp, isRead == 1, priority)

                dataList.add(data)
            }
//            cursor.close()
        }

        db.close()
        return dataList
    }

    private fun sortByQuery(
        sortByTimeItemPosition: Int = -1,
        sortByNotificationPriorityItemPosition: Int = -1,
        sortByCriticalityItemPosition: Int = -1
    ): String {
        var query = ""
        if (sortByCriticalityItemPosition != -1) {

            query += """
                CASE $FN_ALERT_CURRENT_STATUS 
                WHEN 'critical' THEN 1 
                WHEN 'warning' THEN 2 
                WHEN 'clear' THEN 3 
                ELSE 4 
                END ${if (sortByCriticalityItemPosition == 0) "ASC" else "DESC"}
                """
        } else if (sortByNotificationPriorityItemPosition != -1) {
            query += """
                CASE $FN_PRIORITY
                WHEN '${Priority.HIGH_PRIORITY.shortName}' THEN 1
                WHEN '${Priority.MEDIUM_PRIORITY.shortName}' THEN 2
                WHEN '${Priority.LOW_PRIORITY.shortName}' THEN 3
                ELSE 4
                END ${if (sortByNotificationPriorityItemPosition == 0) "ASC" else "DESC"}
        """
        } else if (sortByTimeItemPosition != -1) {
            query += " $FN_CREATED_AT ${if (sortByTimeItemPosition == 1) "ASC" else "DESC"}"
        }

        return query
    }

    private fun filterByQuery(
        statusFilters: ArrayList<String> = ArrayList(),
        priorityFilters: ArrayList<String> = ArrayList(),
        nodesFilters: ArrayList<String> = ArrayList(),
        classFilters: ArrayList<String> = ArrayList(),
        typeFilters: ArrayList<String> = ArrayList(),
    ): String {
        var query = ""
        var statusArg = ""
        var priorityArg = ""
        var nodesArg = ""
        var classArg = ""
        var typeArg = ""
        var compArg = ""

        if (statusFilters.isNotEmpty()) {
            for (i in statusFilters) {
                statusArg += "$FN_ALERT_CURRENT_STATUS LIKE ('$i') OR "
            }
            statusArg = statusArg.dropLast(4)
        }
        if (priorityFilters.isNotEmpty()) {
            for (i in priorityFilters) {
                priorityArg += "$FN_PRIORITY LIKE ('$i') OR "
            }
            priorityArg = priorityArg.dropLast(4)
        }
        if (nodesFilters.isNotEmpty()) {
            for (i in nodesFilters) {
                nodesArg += "$FN_HOST_ID LIKE ('$i') OR "
            }
            nodesArg = nodesArg.dropLast(4)
        }
        if (classFilters.isNotEmpty()) {
            for (i in classFilters) {
                classArg += "$FN_ALERT_CLASS LIKE ('$i') OR "
            }
            classArg = classArg.dropLast(4)
        }
        if (typeFilters.isNotEmpty()) {
            for (i in typeFilters) {
                typeArg += "$FN_ALERT_TYPE LIKE ('$i') OR "
            }
            typeArg = typeArg.dropLast(4)

            for (i in typeFilters) {
                compArg += "$FN_ALERT_COMPONENT LIKE ('$i') OR "
            }
            compArg = compArg.dropLast(4)
        }

        if (statusFilters.isNotEmpty()) {
            query += " AND ($statusArg)"
        }
        if (priorityFilters.isNotEmpty()) {
            query += " AND ($priorityArg)"
        }
        if (nodesFilters.isNotEmpty()) {
            query += " AND ($nodesArg)"
        }
        if (classFilters.isNotEmpty()) {
            query += " AND ($classArg)"
        }
        if (typeFilters.isNotEmpty()) {
            query += " AND ($typeArg)"
            query += " AND ($compArg)"
        }

        return query
    }

    private fun insertRoomsData(item: HomeNotificationList.Data.Netdata.Room, dataID: Long) {
        val values = ContentValues().apply {
            put(RD_ID, item.id)
            put(RD_NAME, item.name)
            put(RD_DATA_ID, dataID)
        }

        val db = writableDatabase
        db.insert(TABLE_ROOMS_DATA, null, values)
        db.close()
    }

    private fun getRoomsData(id: Long): ArrayList<HomeNotificationList.Data.Netdata.Room> {
        val dataList = ArrayList<HomeNotificationList.Data.Netdata.Room>()
        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_ROOMS_DATA WHERE $RD_DATA_ID = $id"

        val cursor = db.rawQuery(selectQuery, null)
        cursor.use {
            while (cursor.moveToNext()) {
                val roomId = cursor.getString(cursor.getColumnIndexOrThrow(RD_ID))
                val roomName = cursor.getString(cursor.getColumnIndexOrThrow(RD_NAME))
                val data = HomeNotificationList.Data.Netdata.Room(roomId, roomName)
                dataList.add(data)
            }
        }

        db.close()
        return dataList
    }

    fun deleteFetchNotificationOlderThanWeek(date: String) {
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