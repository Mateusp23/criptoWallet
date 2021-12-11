package com.example.cryptowalet.ui.adapter

import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptowalet.repository.BuyDAO
import com.example.cryptowalet.R
import com.example.cryptowalet.models.Buy
import com.example.cryptowalet.ui.views.MainActivity
import kotlinx.android.synthetic.main.action_item.view.txtmoeda
import kotlinx.android.synthetic.main.buy_action_item.view.*
import java.text.DecimalFormat

class BuyAdapter(private val actions: List<Buy>) :
    RecyclerView.Adapter<BuyAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        Log.v("LOG", "onCreate")
        val v = LayoutInflater.from(parent.context).inflate(R.layout.buy_action_item, parent, false)
        val vh = VH(v)

        vh.itemView.setOnClickListener {
        }
        return vh
    }

    override fun getItemCount() = actions.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val action = actions[position]
        var showItens = false

        holder.btn_drop.setOnClickListener {
            val df = DecimalFormat("#0.00")
            val buyDAO = BuyDAO(it.context)
            val qtd = buyDAO.selectQuantity(action.name.toString())
            val value = buyDAO.selectValueInvest(action.name.toString())
            if (!showItens) {
                holder.txt_quantidade.visibility = View.VISIBLE
                holder.txt_quantidade.text = "Quantidade = $qtd"
                holder.txt_valor.visibility = View.VISIBLE
                holder.txt_valor.text = "Valor investido = " + df.format(value)
                holder.txt_data.visibility = View.VISIBLE
                holder.txt_data.text = "Data  da compra: " + action.date

                val layoutParams =
                    holder.barra.layoutParams
                layoutParams.height = 430
                holder.barra.layoutParams = layoutParams;
                showItens = true
            } else {
                holder.txt_quantidade.visibility = View.GONE
                holder.txt_valor.visibility = View.GONE
                holder.txt_data.visibility = View.GONE
                val layoutParams =
                    holder.barra.layoutParams
                layoutParams.height = 210
                holder.barra.layoutParams = layoutParams
                showItens = false
            }
        }

        holder.btn_delete.setOnClickListener(View.OnClickListener {
            val buyDAO = BuyDAO(it.context)
            val action = actions[position]
            val alert =
                AlertDialog.Builder(it.context)
            alert.setTitle("AVISO")
            alert
                .setIcon(R.drawable.ic_alert)
                .setMessage("Deseja realmente remover a ação.")
                .setCancelable(true)
                .setPositiveButton(
                    "Sim",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        buyDAO.delete(action)
                        Toast.makeText(it.context, "Ação removida com sucesso", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(it.context, MainActivity::class.java)
                        it.context.startActivity(intent)
                    }
                )
                .setNegativeButton(
                    "Não",
                    DialogInterface.OnClickListener { DialogInterface, i ->
                    }
                )
            val alertDialog = alert.create()
            alertDialog.show()
        })
        holder.txt_moeda.text = action.name.toString()
        holder.txt_data.text = action.date
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_quantidade: TextView = itemView.txt_quantidade
        var txt_moeda: TextView = itemView.txtmoeda
        var txt_valor: TextView = this.itemView.txt_valor
        var txt_data: TextView = itemView.txt_data
        var btn_delete: ImageView = itemView.btn_delete
        var btn_drop: ImageView = this.itemView.drop
        var barra: View = this.itemView.view3
    }
}