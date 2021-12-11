package com.example.cryptowalet.ui.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptowalet.ui.adapter.BuyAdapter
import com.example.cryptowalet.repository.BuyDAO
import com.example.cryptowalet.R
import com.example.cryptowalet.models.Action
import com.example.cryptowalet.models.Buy
import com.example.cryptowalet.models.Name
import kotlinx.android.synthetic.main.activity_buy.*
import kotlinx.android.synthetic.main.activity_main.fab_add
import kotlinx.android.synthetic.main.activity_main.rv_dados
import kotlinx.android.synthetic.main.activity_main.txtMsg

class BuyActivity: AppCompatActivity() {

    private var actionsList = mutableListOf<Buy>()
    var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy)
        val action = intent.getParcelableExtra<Action>("ativos")
        name = action?.name.toString()

        fab_add.setOnClickListener(View.OnClickListener {
            val enviarNome = Name(action?.name)
            val it = Intent(this, BuyingActivity::class.java)
            it.putExtra("nomeAtivo", enviarNome)
            startActivity(it)
        })
        initRecyclerView()
    }

    private fun initRecyclerView() {
        Log.v("LOG", "Inicia RecyclerView")
        val adapter2 = BuyAdapter(actionsList)
        rv_dados.adapter = adapter2
        val layout = LinearLayoutManager(this)
        rv_dados.layoutManager = layout
    }

    private fun update() {
        val compraDao = BuyDAO(this)
        actionsList.clear()
        actionsList = compraDao.selectName(name)
        txt_codigo.text =
            resources.getString(R.string.quantity_action) + " = " + compraDao.selectSum(name).toString()

        if (actionsList.isEmpty()) {
            rv_dados.visibility = View.GONE
            txtMsg.visibility = View.VISIBLE
            txtMsg.text = getString(R.string.blank_action_add)
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
}