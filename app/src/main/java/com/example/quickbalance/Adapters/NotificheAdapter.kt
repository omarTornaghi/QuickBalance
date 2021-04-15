package com.example.quickbalance.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.quickbalance.DataTypes.NotificaType
import com.example.quickbalance.R


class NotificheAdapter(private val notifiche: ArrayList<NotificaType>): RecyclerView.Adapter<NotificheAdapter.NotificaHolder>(){

    fun addItem(notifica:NotificaType ){ notifiche.add(notifica); notifyItemInserted(notifiche.size-1)}

    override fun getItemViewType(position: Int): Int {
        return if (position == notifiche.size) R.layout.item_notifiche_button_aggiungi else R.layout.item_notifiche_info
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificaHolder {
        val itemView: View
        if (viewType == R.layout.item_notifiche_info) {
            itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.item_notifiche_info,
                parent,
                false
            )
        } else {
            itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.item_notifiche_button_aggiungi,
                parent,
                false
            )
        }
        return NotificaHolder(itemView)
    }

    override fun onBindViewHolder(holder: NotificaHolder, position: Int) {
        if (position == notifiche.size) {
            holder.buttonAggiungi?.setOnClickListener({
                Log.d("XXXX", "AGGIUNGI ITEM")
            })
        } else {
            val notifica: NotificaType = notifiche.get(position)
            holder.textViewInfo?.setText(notifica.descrizione)
            holder.buttonRimuovi?.setOnClickListener {  Log.d("XXXX", "ELIMINA ITEM") }
        }
    }

    override fun getItemCount(): Int {
        return notifiche.size + 1
    }

    class NotificaHolder(v: View) : RecyclerView.ViewHolder(v){
        var textViewInfo:TextView?
        var buttonRimuovi:Button?
        var buttonAggiungi:Button?

        init{
            textViewInfo = v.findViewById(R.id.textViewInfoNotifica)
            buttonRimuovi = v.findViewById(R.id.buttonRimuoviNotifica)
            buttonAggiungi = v.findViewById(R.id.buttonAggiungiNotifica)
        }
    }

}