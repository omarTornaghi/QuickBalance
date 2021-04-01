package com.example.quickbalance.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quickbalance.DataTypes.DestinatarioType
import com.example.quickbalance.R
import kotlinx.android.synthetic.main.card_destinatario.view.*

class DestinatarioAdapter(private val destinatari: MutableList<DestinatarioType>, private var textView:TextView) : RecyclerView.Adapter<DestinatarioAdapter.DestinatarioHolder>(){
    fun updateTasks(nuovaLista: List<DestinatarioType>)
    {
        destinatari.clear()
        destinatari.addAll(nuovaLista)
        notifyDataSetChanged()
    }

    fun addItem(item:DestinatarioType){
        destinatari.add(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinatarioHolder
    {
        return DestinatarioHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_destinatario,
                parent,
                false
            )
        )
    }
    override fun getItemCount(): Int = destinatari.size
    override fun onBindViewHolder(holder: DestinatarioHolder, position: Int){
        holder.bind(destinatari[position])
        holder.buttonRimuovi.setOnClickListener { removeItem(position) }
        textView.visibility= View.GONE
    }
    fun removeItem(position: Int) {
        destinatari.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position,destinatari.size)
        if(itemCount == 0)
            textView.visibility = View.VISIBLE

    }
    class DestinatarioHolder(v: View) : RecyclerView.ViewHolder(v){
        private var view: View = v
        lateinit var buttonRimuovi: Button
        fun bind(destinatario: DestinatarioType) {
            buttonRimuovi = view.buttonRimuoviDestinatario
            view.textViewGeneralita.text = destinatario.getGeneralita()
            view.textViewTelefono.text = if(destinatario.numeroTelefono.isNullOrBlank()) view.getResources().getString(
                R.string.not_defined
            ) else destinatario.numeroTelefono
        }

    }
}