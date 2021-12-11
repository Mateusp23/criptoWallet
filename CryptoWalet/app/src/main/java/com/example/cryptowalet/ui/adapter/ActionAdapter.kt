package com.example.cryptowalet.ui.adapter

import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptowalet.repository.ActionDAO
import com.example.cryptowalet.repository.BuyDAO
import com.example.cryptowalet.R
import com.example.cryptowalet.models.Action
import com.example.cryptowalet.ui.views.BuyActivity
import com.example.cryptowalet.ui.views.MainActivity
import kotlinx.android.synthetic.main.action_item.view.*
import java.text.DecimalFormat

class ActionAdapter(private val actions: List<Action>) :
    RecyclerView.Adapter<ActionAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.action_item, parent, false)
        val vh = VH(v)

        vh.itemView.setOnClickListener {
            val action = actions[vh.adapterPosition]
            val it = Intent(parent.context, BuyActivity::class.java)
            it.putExtra("ativos", action)
            parent.context.startActivity(it)
        }
        return vh
    }

    override fun getItemCount(): Int {
        return actions.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val actions = actions[position]
        var showItens = false
        holder.btn_delete.setOnClickListener(View.OnClickListener {
            val actionDAO = ActionDAO(it.context)
            val action = this.actions[position]
            val alert =
                AlertDialog.Builder(it.context)
            alert.setTitle("Aviso")
            alert
                .setIcon(R.drawable.ic_alert)
                .setMessage("Deseja realmente remover o ativo.")
                .setCancelable(true)
                .setPositiveButton(
                    "Sim",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        actionDAO.delete(action)
                        Toast.makeText(it.context, "Ativo removido com sucesso", Toast.LENGTH_SHORT)
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

        holder.btn_drop.setOnClickListener {
            val df = DecimalFormat("#0.00")
            val buyDAO = BuyDAO(it.context)
            val qtd = buyDAO.selectQuantity(actions.name.toString())
            val valor = buyDAO.selectValueInvest(actions.name.toString())
            if (!showItens) {

                holder.txt_quantidade.visibility = View.VISIBLE
                holder.txt_quantidade.text = "Quantidade = " + qtd.toString()
                holder.txt_valor.visibility = View.VISIBLE
                holder.txt_valor.text = "Valor investido = " + df.format(valor)

                val layoutParams =
                    holder.barra.layoutParams
                layoutParams.height = 330
                holder.barra.layoutParams = layoutParams;
                showItens = true
            } else {
                holder.txt_quantidade.visibility = View.GONE
                holder.txt_valor.visibility = View.GONE
                val layoutParams =
                    holder.barra.layoutParams
                layoutParams.height = 210
                holder.barra.layoutParams = layoutParams
                showItens = false
            }
        }
        holder.txtCodigo.text = actions.cod.toString()
        holder.txtMoeda.text = actions.name.toString()
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtCodigo: TextView = itemView.txt_codigo
        var txtMoeda: TextView = itemView.txtmoeda
        var btn_delete: ImageView = this.itemView.btn_delete
        var btn_drop: ImageView = this.itemView.drop
        var txt_quantidade: TextView = this.itemView.txt_quantidade
        var txt_valor: TextView = this.itemView.txt_valor
        var barra: View = this.itemView.view3
    }
}