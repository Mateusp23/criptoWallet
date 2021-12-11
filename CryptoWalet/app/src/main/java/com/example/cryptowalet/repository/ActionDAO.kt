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
        contextValues.put(NOME_COLUMN, action.name)
        contextValues.put(CODIGO_COLUMN, action.cod)
        contextValues.put(QTD_COLUMN, action.quantity)

        val respId = db.insert(TABELA_ATIVO, null, contextValues)
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
        return db.delete(TABELA_ATIVO, "id_ativos =?", arrayOf(action.id.toString()))
    }

    fun select(): ArrayList<Action> {
        Log.v("LOG", "GetAll")
        val db = dbHelper.writableDatabase
        val sql = "SELECT * from $TABELA_ATIVO"
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

        val sql = "SELECT * from $TABELA_ATIVO"
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
        val cod = cursor.getString(cursor.getColumnIndex(CODIGO_COLUMN))
        val name = cursor.getString(cursor.getColumnIndex(NOME_COLUMN))
        val quantity = cursor.getDouble(cursor.getColumnIndex(QTD_COLUMN))

        return Action(id, name, cod, quantity)
    }
}