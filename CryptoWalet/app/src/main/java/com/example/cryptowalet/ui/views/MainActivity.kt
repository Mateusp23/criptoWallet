package com.example.cryptowalet.ui.views

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptowalet.*
import com.example.cryptowalet.service.CriptoHTTP
import com.example.cryptowalet.models.Action
import com.example.cryptowalet.models.Criptos
import com.example.cryptowalet.repository.ActionDAO
import com.example.cryptowalet.repository.BuyDAO
import com.example.cryptowalet.ui.adapter.ActionAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat


class MainActivity : AppCompatActivity() {

    private var actionsList = mutableListOf<Action>()
    private var asyncTask: StatesTask? = null

    var valorLTC = 0.0
    var valorBTC = 0.0
    var valorXRP = 0.0
    var valorBCH = 0.0
    var valorETH = 0.0
    var qtdBTC = 0.0
    var qtdLTC = 0.0
    var qtdBCH = 0.0
    var qtdXRP = 0.0
    var qtdETH = 0.0
    var total = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab_add.setOnClickListener(View.OnClickListener {
            val it = Intent(this, ActionsActivity::class.java)
            startActivity(it)
        })
        initRecyclerView()
        loadingData()
    }

    private fun initRecyclerView() {
        val adapter2 = ActionAdapter(actionsList)
        rv_dados.adapter = adapter2
        val layout = LinearLayoutManager(this)
        rv_dados.layoutManager = layout
    }

    private fun update() {
        val df = DecimalFormat("#0.00")
        val actionDAO = ActionDAO(this)
        loadingData()
        actionsList.clear()
        actionsList = actionDAO.select()
        val buysDAO = BuyDAO(this)
        total = buysDAO.selectTotal()
        qtdBTC = buysDAO.selectQuantity("Bitcoin")
        qtdLTC = buysDAO.selectQuantity("Litecoin")
        qtdBCH = buysDAO.selectQuantity("BCash")
        qtdXRP = buysDAO.selectQuantity("XRP")
        qtdETH = buysDAO.selectQuantity("Ethereum")
        txt_total.text = getResources().getString(R.string.quantity_invest) + " = " + df.format(total)

        if (actionsList.isEmpty()) {
            rv_dados.visibility = View.GONE
            txtMsg.visibility = View.VISIBLE
            txtMsg.text = "Nenhum ativo adicionado"
        } else {
            rv_dados.visibility = View.VISIBLE
            txtMsg.visibility = View.GONE

        }
        rv_dados.adapter?.notifyDataSetChanged()
    }

    override fun onRestart() {
        super.onRestart()
        update()
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        update()
        initRecyclerView()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menu_refresh -> {
            Toast.makeText(this, "Carregando...", Toast.LENGTH_LONG).show()
            loadingData()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun loadingData() {
        if (asyncTask == null) {
            if (CriptoHTTP.hasConnection(this)) {
                if (asyncTask?.status != AsyncTask.Status.RUNNING) {
                    asyncTask = StatesTask()
                    asyncTask?.execute()
                }
            } else {
                img_net_check.visibility = View.VISIBLE
                txt_net_check.visibility = View.VISIBLE
                txt_variacao.visibility = View.GONE
                txt_totalMoedas.visibility = View.GONE
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class StatesTask : AsyncTask<Void, Void, ArrayList<Criptos>?>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        @SuppressLint("WrongThread")
        @RequiresApi(Build.VERSION_CODES.O)
        override fun doInBackground(vararg params: Void?): ArrayList<Criptos>? {
            return CriptoHTTP.loadCriptos()
        }

        private fun update(result: ArrayList<Criptos>?) {
            val df = DecimalFormat("#0.00")
            if (result != null) {

                valorBTC = result[0].buy.toDouble()
                valorBTC *= qtdBTC
                valorLTC = result[1].buy.toDouble()
                valorLTC *= qtdLTC
                valorXRP = result[2].buy.toDouble()
                valorXRP *= qtdXRP
                valorBCH = result[3].buy.toDouble()
                valorBCH *= qtdBCH
                valorETH *= qtdETH
                var variation = 0.0
                val valorTotal = valorBTC + valorLTC + valorBCH + valorXRP + valorETH
                if (total > 0.0) {
                    variation = (valorTotal / total - 1) * 100
                }
                img_net_check.visibility = View.GONE
                txt_net_check.visibility = View.GONE

                txt_totalMoedas.text =
                    getResources().getString(R.string.quantity_total) + " = " + df.format(valorTotal)

                val text =
                    resources.getString(R.string.variations) + " = " + df.format(variation) + "%"
                val ss = SpannableString(text)

                if (variation < 0) {
                    val fcRed = ForegroundColorSpan(Color.parseColor("#D5EE0800"))
                    ss.setSpan(fcRed, 11, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                } else {
                    val fcGreen = ForegroundColorSpan(Color.parseColor("#03AE06"))
                    ss.setSpan(fcGreen, 11, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                txt_variacao.text = ss
                txt_variacao.visibility = View.VISIBLE
                txt_totalMoedas.visibility = View.VISIBLE
            }

            asyncTask = null
        }

        override fun onPostExecute(result: ArrayList<Criptos>?) {
            super.onPostExecute(result)
            if (result != null) {
                update(result as ArrayList<Criptos>)
            } else {
                img_net_check.visibility = View.VISIBLE
                txt_net_check.visibility = View.VISIBLE
                txt_variacao.visibility = View.GONE
                txt_totalMoedas.visibility = View.GONE
            }
        }
    }
}