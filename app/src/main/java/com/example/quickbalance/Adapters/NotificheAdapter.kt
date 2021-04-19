package com.example.quickbalance.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.quickbalance.DataTypes.NotificaType
import com.example.quickbalance.R

class NotificheAdapter(private val notifiche: ArrayList<NotificaType>, val context: Context): RecyclerView.Adapter<NotificheAdapter.NotificaHolder>(){
    private val MAX_NOTIFICHE:Int = 5
    fun addItem(notifica:NotificaType ){
        if(notifiche.size < MAX_NOTIFICHE) {
            notifiche.add(notifica)
            notifyItemInserted(notifiche.size - 1)
        }
    }

    fun removeItem(position: Int) {
        notifiche.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, notifiche.size)
    }

    fun updateItem(position:Int, newN:NotificaType){
        notifiche.set(position, newN)
        notifyItemChanged(position)
    }

    fun getList():ArrayList<NotificaType>{return notifiche}

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
            holder.buttonAggiungi?.setOnClickListener {
                var n = NotificaType(0, context)
                if (notifiche.size > 0)
                    n = notifiche.get(notifiche.size - 1)
                addItem(NotificaType((((n.numGiorni + 1)-1) % 7)+1, context))
            }
        } else {
            val notifica: NotificaType = notifiche.get(position)
            holder.textViewInfo?.setText(notifica.toString())
            holder.textViewInfo?.setOnClickListener{creaDialog(position)}
            holder.buttonRimuovi?.setOnClickListener { removeItem(position) }
            holder.layout?.setOnClickListener{creaDialog(position)}
        }
    }

    private fun creaDialog(position:Int){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.add_reminder))
        var list:ArrayList<String> = ArrayList()
        list.add(context.getString(R.string.nothing))
        for(i in 1..7){
            list.add(NotificaType(i, context).toString())
        }

        builder.setItems(list.toTypedArray()) { dialog, which ->
            when (which) {
                0 -> {  removeItem(position)}
                else -> {
                    updateItem(position, NotificaType(which, context))
                }
            }
        }
        builder.setNegativeButton(R.string.cancel, { dialog, which ->  dialog.dismiss()})
        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    override fun getItemCount(): Int {
        return notifiche.size + 1
    }

    class NotificaHolder(v: View) : RecyclerView.ViewHolder(v){
        var layout:ConstraintLayout?
        var textViewInfo:TextView?
        var buttonRimuovi:Button?
        var buttonAggiungi:Button?

        init{
            layout = v.findViewById(R.id.clItemNotifica)
            textViewInfo = v.findViewById(R.id.textViewInfoNotifica)
            buttonRimuovi = v.findViewById(R.id.buttonRimuoviNotifica)
            buttonAggiungi = v.findViewById(R.id.buttonAggiungiNotifica)
        }
    }

}