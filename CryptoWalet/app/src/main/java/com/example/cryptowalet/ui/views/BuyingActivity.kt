package com.example.cryptowalet.ui.views

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptowalet.repository.BuyDAO
import com.example.cryptowalet.R
import com.example.cryptowalet.service.CriptoBuyHTTP
import com.example.cryptowalet.models.Buy
import com.example.cryptowalet.models.Criptos
import com.example.cryptowalet.models.Name
import kotlinx.android.synthetic.main.activity_buying.*
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class BuyingActivity : AppCompatActivity() {

    private var asyncTask: StatesTask? = null
    var cod: String = ""
    var valueT = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buying)

        val buy = intent.getParcelableExtra<Name>("nomeAtivo")
        if (buy?.name.equals("Bitcoin")) {
            cod = "BTC"
        }
        if (buy?.name.equals("Litecoin")) {
            cod = "LTC"
        }
        if (buy?.name.equals("XRP")) {
            cod = "XRP"
        }
        if (buy?.name.equals("BCash")) {
            cod = "BCH"
        }
        if (buy?.name.equals("Ethereum")) {
            cod = "ETH"
        }
        loadingData()

        FABBack.setOnClickListener {
            onBackPressed()
        }

        FABSave.setOnClickListener {
            if (edtQtd.text.toString().toFloat() > 0) {
                try {
                    val verificacao = edtQtd.text.toString().toFloat()
                    val dStr = getDate()
                    val compra = Buy(
                        null,
                        buy?.name.toString(),
                        dStr.toString(),
                        edtQtd.text.toString().toDouble(),
                        valueT
                    )
                    val compraDao = BuyDAO(this)
                    compraDao.insert(compra)
                    onBackPressed()
                } catch (e: Exception) {
                    alert()
                }
            } else {
                alert()
            }
        }
    }

    fun alert() {
        val alert =
            AlertDialog.Builder(this@BuyingActivity)
        alert.setTitle("Aviso")
        alert
            .setIcon(R.drawable.ic_alert)
            .setMessage("Você deve digitar apenas numeros maiores que 0.")
            .setCancelable(true)
            .setPositiveButton(
                "OK",
                DialogInterface.OnClickListener { dialogInterface, i ->
                }
            )
        val alertDialog = alert.create()
        alertDialog.show()
    }

    fun loadingData() {
        if (asyncTask == null) {
            if (CriptoBuyHTTP.hasConnection(this)) {
                if (asyncTask?.status != AsyncTask.Status.RUNNING) {
                    asyncTask = StatesTask()
                    asyncTask?.execute()
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class StatesTask : AsyncTask<Void, Void, Criptos?>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        @SuppressLint("WrongThread")
        @RequiresApi(Build.VERSION_CODES.O)
        override fun doInBackground(vararg params: Void?): Criptos? {
            return CriptoBuyHTTP.loadCripto(cod)
        }

        private fun update(result: Criptos?) {
            val df = DecimalFormat("#0.00")
            if (result != null) {
                valueT = result.buy.toDouble()
            }
            txt_valor.text = resources.getString(R.string.value_action) + " R$ " + df.format(valueT)

            asyncTask = null
        }

        override fun onPostExecute(result: Criptos?) {
            super.onPostExecute(result)
            update(result as Criptos?)
        }
    }

    private fun getDate(): String? {
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = Date()
        return dateFormat.format(date)
    }
}
