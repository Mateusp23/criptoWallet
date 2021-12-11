package com.example.cryptowalet.ui.views

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.cryptowalet.repository.ActionDAO
import com.example.cryptowalet.R
import com.example.cryptowalet.models.Action
import kotlinx.android.synthetic.main.activity_actions.*

class ActionsActivity : AppCompatActivity() {

    var name = ""
    var cod = ""
    var quantity = 0.0
    var idSelected: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actions)

        btn_add_btc.setOnClickListener {
            name = "Bitcoin"
            cod = "BTC"
            idSelected = 1
            checkId()
        }

        btn_add_ltc.setOnClickListener {
            name = "Litecoin"
            cod = "LTC"
            idSelected = 2
            checkId()
        }

        btn_add_bch.setOnClickListener {
            name = "BCash"
            cod = "BHC"
            idSelected = 3
            checkId()
        }

        btn_add_xrp.setOnClickListener {
            name = "XRP"
            cod = "XRP"
            idSelected = 4
            checkId()
        }

        btn_add_eth.setOnClickListener {
            name = "Ethereum"
            cod = "ETH"
            idSelected = 5
            checkId()
        }
    }

    private fun checkId() {
        val action = Action(idSelected, name, cod, quantity)
        val actionDAO = ActionDAO(this)
        val checkingID = actionDAO.findId(idSelected)
        if (checkingID) {
            actionDAO.insert(action)
            onBackPressed()
        } else {
            val alert =
                AlertDialog.Builder(this@ActionsActivity)
            alert.setTitle("Aviso")
            alert
                .setIcon(R.drawable.ic_alert)
                .setMessage("Ativo já adicionado.")
                .setCancelable(true)
                .setPositiveButton(
                    "OK",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                    }
                )
            val alertDialog = alert.create()
            alertDialog.show()
        }
    }
}