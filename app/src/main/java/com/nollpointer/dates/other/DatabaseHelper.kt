package com.nollpointer.dates.other

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE D10")
        db.execSQL("DROP TABLE D1")
        onCreate(db)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL("DROP TABLE D10")
        sqLiteDatabase.execSQL("DROP TABLE D1")
        onCreate(sqLiteDatabase)
    }

/*
    private fun insertDate(db: SQLiteDatabase, name: String, date: String, event: String) {
        val ct = ContentValues()
        ct.put("DATE", date)
        ct.put("EVENT", event)
        ct.put("REQUEST", "y")
        db.insert(name, null, ct)
    }
*/

    private fun insertDate(db: SQLiteDatabase, name: String, date: String, event: String, request: String, category: Int) {
        val ct = ContentValues()
        ct.put("DATE", date)
        ct.put("EVENT", event)
        ct.put("REQUEST", request)
        ct.put("CATEGORY", category)
        db.insert(name, null, ct)
    }

    private fun parseString(db: SQLiteDatabase, table_name: String, text: String) {
        val scan = Scanner(text)
        var str: Array<String>
        while (scan.hasNextLine()) {
            str = scan.nextLine().split("#").toTypedArray()
            insertDate(db, table_name, str[0].trim { it <= ' ' }, str[1].trim { it <= ' ' }, str[2].trim { it <= ' ' }, str[3].toInt())
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE D10 (_id INTEGER PRIMARY KEY AUTOINCREMENT,DATE TEXT, EVENT DATE, REQUEST TEXT, CATEGORY INTEGER);")
        db.execSQL("CREATE TABLE D1 (_id INTEGER PRIMARY KEY AUTOINCREMENT,DATE TEXT, EVENT DATE, REQUEST TEXT, CATEGORY INTEGER);")
    }

    companion object {

        /*
        * Основная информация по БД
        */
        private const val DB_NAME = "statistics"
        private const val DB_VERSION = 1

        /*
       * Названия таблиц
       */
        private const val APP_STATISTICS = "AppStatistics"

        private const val CARDS_STATISTICS = "CardsStatistics"
        private const val VOICE_STATISTICS = "VoiceStatistics"
        private const val TEST_STATISTICS = "TestStatistics"
        private const val TRUE_FALSE_STATISTICS = "TrueFalseStatistics"
        private const val SORT_STATISTICS = "SortStatistics"
        private const val DISTRIBUTION_STATISTICS = "DistributionStatistics"

        /*
        * Поля БД
        */
        private const val ID = "_id"

    }

}