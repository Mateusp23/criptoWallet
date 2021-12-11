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
import com.example.cryptowalet.service.CryptoBuyHTTP
import com.example.cryptowalet.models.Buy
import com.example.cryptowalet.models.Crypto
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
                    val check = edtQtd.text.toString().toFloat()
                    val dStr = getDate()
                    val buy = Buy(
                        null,
                        buy?.name.toString(),
                        dStr.toString(),
                        edtQtd.text.toString().toDouble(),
                        valueT
                    )
                    val buyDAO = BuyDAO(this)
                    buyDAO.insert(buy)
                    onBackPressed()
                } catch (e: Exception) {
                    alert()
                }
            } else {
                alert()
            }
        }
    }

    private fun alert() {
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

    private fun loadingData() {
        if (asyncTask == null) {
            if (CryptoBuyHTTP.hasConnection(this)) {
                if (asyncTask?.status != AsyncTask.Status.RUNNING) {
                    asyncTask = StatesTask()
                    asyncTask?.execute()
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class StatesTask : AsyncTask<Void, Void, Crypto?>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        @SuppressLint("WrongThread")
        @RequiresApi(Build.VERSION_CODES.O)
        override fun doInBackground(vararg params: Void?): Crypto? {
            return CryptoBuyHTTP.loadCrypto(cod)
        }

        private fun update(result: Crypto?) {
            val df = DecimalFormat("#0.00")
            if (result != null) {
                valueT = result.buy.toDouble()
            }
            txt_valor.text = resources.getString(R.string.value_action) + " R$ " + df.format(valueT)

            asyncTask = null
        }

        override fun onPostExecute(result: Crypto?) {
            super.onPostExecute(result)
            update(result as Crypto?)
        }
    }

    private fun getDate(): String? {
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = Date()
        return dateFormat.format(date)
    }
}
