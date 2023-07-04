package com.netdata.app.utils.localdb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.legacy.widget.Space
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.netdata.app.data.pojo.response.SpaceList

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "netdata.db"
        private const val DATABASE_VERSION = 1

        // Define your table names and column names here
        private const val TABLE_SPACE = "space"
        private const val TABLE_NAME_2 = "table2"

        // Define column names for table 1
        private const val ID = "id"
        private const val SLUG = "slug"
        private const val NAME = "name"
        private const val DESCRIPTION = "description"
        private const val ICON_URL = "iconURL"
        private const val CREATED_AT = "createdAt"
        private const val PERMISSIONS = "permissions"
        private const val PLAN = "plan"
        private const val PLAN_DEFINITION = "planDefinition"

        // Define column names for table 2
        private const val COLUMN_ID_2 = "id"
        private const val COLUMN_TITLE_2 = "title"
        private const val COLUMN_CONTENT_2 = "content"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable1Query =
            "CREATE TABLE $TABLE_SPACE ($ID TEXT, $SLUG TEXT, $NAME TEXT, $DESCRIPTION TEXT, $ICON_URL TEXT, $CREATED_AT TEXT, $PERMISSIONS TEXT, $PLAN TEXT, $PLAN_DEFINITION TEXT)"
        db.execSQL(createTable1Query)

        val createTable2Query =
            "CREATE TABLE $TABLE_NAME_2 ($COLUMN_ID_2 INTEGER PRIMARY KEY, $COLUMN_TITLE_2 TEXT, $COLUMN_CONTENT_2 TEXT)"
        db.execSQL(createTable2Query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Perform necessary migrations or updates here
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SPACE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_2")
        onCreate(db)
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

    fun insertDataIntoTable2(title: String, content: String) {
        val values = ContentValues().apply {
            put(COLUMN_TITLE_2, title)
            put(COLUMN_CONTENT_2, content)
        }

        val db = writableDatabase
        db.insert(TABLE_NAME_2, null, values)
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

    fun getAllDataFromTable2(): List<Pair<String, String>> {
        val dataList = mutableListOf<Pair<String, String>>()
        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME_2"

        val cursor = db.rawQuery(selectQuery, null)
        cursor.use {
            while (cursor.moveToNext()) {
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE_2))
                val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT_2))
                val data = Pair(title, content)
                dataList.add(data)
            }
        }

        db.close()
        return dataList
    }
}