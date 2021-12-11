package com.example.cryptowalet.helpers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.cryptowalet.constants.*

private const val DB_NAME = "Portfolio.db"
private const val DB_VERSION = 9

class DbHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val sql = "CREATE TABLE $TABLE_ACTION($ID_COLUMN  INTEGER PRIMARY KEY " +
                "AUTOINCREMENT, $NAME_COLUMN TEXT,$COD_COLUMN TEXT, $QUANTITY_COLUMN DOUBLE);"
        db.execSQL(sql)
        val sql2 = " CREATE TABLE $TABLE_BUY($ID_BUY INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$NAME_BUY TEXT, $DATE_BUY TEXT, $QUANTITY_BUY DOUBLE, $BUY_VALUE DOUBLE)"
        db.execSQL(sql2)

        Log.e("LOG", "Criando")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ACTION")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BUY")
        onCreate(db)
    }
}