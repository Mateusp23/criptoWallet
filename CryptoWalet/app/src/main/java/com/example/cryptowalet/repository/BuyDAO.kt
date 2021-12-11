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

        contextValues.put(ID_COMPRA, buy.id)
        contextValues.put(NOME_COMPRA, buy.name)
        contextValues.put(DATA_COMPRA, buy.date)
        contextValues.put(QTD_COMPRA, buy.quantity)
        contextValues.put(VALOR_COMPRA, buy.value)

        val respId = db.insert(TABELA_COMPRA, null, contextValues)
        val message = if (respId != -1L) {
            "Dados inseridos"
        } else {
            "Erro ao inserir dados"
        }
        db.close()
        return message
    }

    fun selectName(nome: String): ArrayList<Buy> {
        Log.v("LOG", "GetAll")
        val db = dbHelper.writableDatabase
        val sql = "SELECT * from $TABELA_COMPRA where $NOME_COMPRA like '%$nome%' "
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
        val sql = "SELECT * from $TABELA_COMPRA where $NOME_COMPRA like '%$nome%' "
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
        val sql = "SELECT * from $TABELA_COMPRA"
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
        val sql = "SELECT * from $TABELA_COMPRA where $NOME_COMPRA like '%$nome%' "
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
        return db.delete(TABELA_COMPRA, "id_compra =?", arrayOf(buy.id.toString()))
    }

    fun selectSum(nome: String): Double {
        Log.v("LOG", "GetAll")
        val db = dbHelper.writableDatabase
        val sql = "SELECT * from $TABELA_COMPRA where $NOME_COMPRA like '%$nome%' "
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
        val id = cursor.getInt(cursor.getColumnIndex(ID_COMPRA))
        val name = cursor.getString(cursor.getColumnIndex(NOME_COMPRA))
        val date = cursor.getString(cursor.getColumnIndex(DATA_COMPRA))
        val quantity = cursor.getDouble(cursor.getColumnIndex(QTD_COMPRA))
        val value = cursor.getDouble(cursor.getColumnIndex(VALOR_COMPRA))

        return Buy(id, name, date, quantity, value)
    }
}
