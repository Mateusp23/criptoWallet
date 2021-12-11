package com.example.cryptowalet.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.example.cryptowalet.constants.*
import com.example.cryptowalet.helpers.DbHelper
import com.example.cryptowalet.models.Action

class ActionDAO(context: Context) {
    private val dbHelper = DbHelper(context)

    fun insert(action: Action): String {
        val db = dbHelper.writableDatabase
        val contextValues = ContentValues()

        contextValues.put(ID_COLUMN, action.id)
        contextValues.put(NAME_COLUMN, action.name)
        contextValues.put(COD_COLUMN, action.cod)
        contextValues.put(QUANTITY_COLUMN, action.quantity)

        val respId = db.insert(TABLE_ACTION, null, contextValues)
        val message = if (respId != -1L) {
            "Dados inseridos"
        } else {
            "Erro ao inserir dados"
        }
        db.close()
        return message
    }

    fun delete(action: Action): Int {
        val db = dbHelper.writableDatabase
        return db.delete(TABLE_ACTION, "id_ativos =?", arrayOf(action.id.toString()))
    }

    fun select(): ArrayList<Action> {
        val db = dbHelper.writableDatabase
        val sql = "SELECT * from $TABLE_ACTION"
        val index = db.rawQuery(sql, null)
        val action = ArrayList<Action>()
        while (index.moveToNext()) {
            val coin = actionIndex(index)
            action.add(coin)
        }
        index.close()
        db.close()
        Log.v("LOG", "Get ${action.size}")
        return action
    }

    fun findId(actionId: Int?): Boolean {
        val db = dbHelper.writableDatabase
        var id = 0

        val sql = "SELECT * from $TABLE_ACTION"
        val index = db.rawQuery(sql, null)
        val action = ArrayList<Action>()
        while (index.moveToNext()) {
            val coin = actionIndex(index)
            id = coin.id!!
            if (actionId == id) {
                return false
            }
        }
        index.close()
        db.close()
        Log.v("LOG", "Get ${action.size}")
        return true
    }

    @SuppressLint("Range")
    private fun actionIndex(cursor: Cursor): Action {
        val id = cursor.getInt(cursor.getColumnIndex(ID_COLUMN))
        val cod = cursor.getString(cursor.getColumnIndex(COD_COLUMN))
        val name = cursor.getString(cursor.getColumnIndex(NAME_COLUMN))
        val quantity = cursor.getDouble(cursor.getColumnIndex(QUANTITY_COLUMN))

        return Action(id, name, cod, quantity)
    }
}