package com.netdata.app.utils.localdb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.legacy.widget.Space
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.netdata.app.data.pojo.response.AppDetails
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

        // Define column names for TABLE_SPACE
        private const val ID = "id"
        private const val SLUG = "slug"
        private const val NAME = "name"
        private const val DESCRIPTION = "description"
        private const val ICON_URL = "iconURL"
        private const val CREATED_AT = "createdAt"
        private const val PERMISSIONS = "permissions"
        private const val PLAN = "plan"
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
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableSpaceQuery =
            "CREATE TABLE $TABLE_SPACE ($ID TEXT, $SLUG TEXT, $NAME TEXT, $DESCRIPTION TEXT, $ICON_URL TEXT, $CREATED_AT TEXT, $PERMISSIONS TEXT, $PLAN TEXT, $PLAN_DEFINITION TEXT )"
        db.execSQL(createTableSpaceQuery)

        val createTableNotificationPriorityQuery =
            "CREATE TABLE $TABLE_NOTIFICATION_PRIORITY ($NP_ID INTEGER, $NP_PRIORITY TEXT, $NP_IS_SOUND INTEGER, $NP_SOUND_NAME TEXT, $NP_SOUND_URL TEXT, $NP_IS_BANNER INTEGER, $NP_IS_VIBRATION INTEGER )"
        db.execSQL(createTableNotificationPriorityQuery)

        val createTableAppDetailsQuery =
            "CREATE TABLE $TABLE_APP_DETAILS ($AD_ID INTEGER, $AD_SI_COOKIE TEXT, $AD_SV_COOKIE TEXT, $AD_IS_LOGIN INTEGER, $AD_IS_NOTIFICATION_PRIORITY_DATA INTEGER )"
        db.execSQL(createTableAppDetailsQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Perform necessary migrations or updates here
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SPACE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTIFICATION_PRIORITY")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_APP_DETAILS")
        onCreate(db)
    }

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

    fun insertSpaceData(item: SpaceList) {
        val values = ContentValues().apply {
            put(ID, item.id)
            put(SLUG, item.slug)
            put(NAME, item.name)
            put(DESCRIPTION, item.description)
            put(ICON_URL, item.iconURL)
            put(CREATED_AT, item.createdAt)
            put(PERMISSIONS, Gson().toJson(item.permissions))
            put(PLAN, item.plan)
            put(PLAN_DEFINITION, Gson().toJson(item.planDefinition))
        }

        val db = writableDatabase
        db.insert(TABLE_SPACE, null, values)
        db.close()
    }

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

    fun getAllDataFromApp(): ArrayList<AppDetails> {
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
                val plan = cursor.getString(cursor.getColumnIndexOrThrow(PLAN))
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
}