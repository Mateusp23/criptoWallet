package com.example.cryptowalet.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.example.cryptowalet.constants.*
import com.example.cryptowalet.helpers.DbHelper
import com.example.cryptowalet.models.Buy

class BuyDAO(context: Context) {
    private val dbHelper = DbHelper(context)

    fun insert(buy: Buy): String {
        val db = dbHelper.writableDatabase
        val contextValues = ContentValues()

        contextValues.put(ID_BUY, buy.id)
        contextValues.put(NAME_BUY, buy.name)
        contextValues.put(DATE_BUY, buy.date)
        contextValues.put(QUANTITY_BUY, buy.quantity)
        contextValues.put(BUY_VALUE, buy.value)

        val respId = db.insert(TABLE_BUY, null, contextValues)
        val message = if (respId != -1L) {
            "Dados inseridos"
        } else {
            "Erro ao inserir dados"
        }
        db.close()
        return message
    }

    fun selectName(nome: String): ArrayList<Buy> {
        val db = dbHelper.writableDatabase
        val sql = "SELECT * from $TABLE_BUY where $NAME_BUY like '%$nome%' "
        Log.v("LOG", "" + sql)
        val index = db.rawQuery(sql, null)
        val buy = ArrayList<Buy>()
        while (index.moveToNext()) {
            val buys = buyIndex(index)
            buy.add(buys)
        }
        index.close()
        db.close()
        Log.v("LOG", "Get ${buy.size}")
        return buy
    }

    fun selectQuantity(nome: String): Double {
        Log.v("LOG", "GetAll")
        val db = dbHelper.writableDatabase
        val sql = "SELECT * from $TABLE_BUY where $NAME_BUY like '%$nome%' "
        Log.v("LOG", "" + sql)
        val index = db.rawQuery(sql, null)
        var quantity = 0.0
        while (index.moveToNext()) {
            val buys = buyIndex(index)
            quantity += buys.quantity!!
        }
        index.close()
        db.close()
        return quantity
    }

    fun selectTotal(): Double {
        Log.v("LOG", "GetAll")
        val db = dbHelper.writableDatabase
        val sql = "SELECT * from $TABLE_BUY"
        Log.v("LOG", "" + sql)
        val index = db.rawQuery(sql, null)
        val buy = ArrayList<Buy>()
        var total = 0.0
        while (index.moveToNext()) {
            val buy = buyIndex(index)
            val qtd = buy.quantity!!
            total += qtd * buy.value!!
        }
        index.close()
        db.close()
        Log.v("LOG", "Get ${buy.size}")
        return total
    }

    fun selectValueInvest(nome: String): Double {
        Log.v("LOG", "GetAll")
        val db = dbHelper.writableDatabase
        val sql = "SELECT * from $TABLE_BUY where $NAME_BUY like '%$nome%' "
        Log.v("LOG", "" + sql)
        val index = db.rawQuery(sql, null)
        val buy = ArrayList<Buy>()
        var total = 0.0
        while (index.moveToNext()) {
            val buy1 = buyIndex(index)
            val quantity = buy1.quantity!!
            total += quantity * buy1.value!!
        }
        index.close()
        db.close()
        Log.v("LOG", "Get ${buy.size}")
        return total
    }

    fun delete(buy: Buy): Int {
        val db = dbHelper.writableDatabase
        return db.delete(TABLE_BUY, "id_compra =?", arrayOf(buy.id.toString()))
    }

    fun selectSum(nome: String): Double {
        Log.v("LOG", "GetAll")
        val db = dbHelper.writableDatabase
        val sql = "SELECT * from $TABLE_BUY where $NAME_BUY like '%$nome%' "
        Log.v("LOG", "" + sql)
        val index = db.rawQuery(sql, null)
        val buy = ArrayList<Buy>()
        var sum = 0.0
        while (index.moveToNext()) {
            val buy1 = buyIndex(index)
            sum += buy1.quantity!!
        }
        index.close()
        db.close()
        Log.v("LOG", "Get ${buy.size}")
        return sum
    }

    @SuppressLint("Range")
    private fun buyIndex(cursor: Cursor): Buy {
        val id = cursor.getInt(cursor.getColumnIndex(ID_BUY))
        val name = cursor.getString(cursor.getColumnIndex(NAME_BUY))
        val date = cursor.getString(cursor.getColumnIndex(DATE_BUY))
        val quantity = cursor.getDouble(cursor.getColumnIndex(QUANTITY_BUY))
        val value = cursor.getDouble(cursor.getColumnIndex(BUY_VALUE))

        return Buy(id, name, date, quantity, value)
    }
}
