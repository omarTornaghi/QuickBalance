package com.example.quickbalance.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quickbalance.DataTypes.PartecipanteType
import com.example.quickbalance.R
import kotlinx.android.synthetic.main.card_partecipante.view.*

class PartecipanteAdapter(private val partecipanti: ArrayList<PartecipanteType>, private var textView:TextView, private var button:Button) : RecyclerView.Adapter<PartecipanteAdapter.DestinatarioHolder>(){
    fun updateTasks(nuovaLista: ArrayList<PartecipanteType>)
    {
        partecipanti.clear()
        partecipanti.addAll(nuovaLista)
        notifyDataSetChanged()
    }

    fun addItem(item:PartecipanteType){
        partecipanti.add(item)
        notifyDataSetChanged()
    }

    fun getList(): ArrayList<PartecipanteType> { return partecipanti}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinatarioHolder
    {
        return DestinatarioHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_partecipante,
                parent,
                false
            )
        )
    }
    override fun getItemCount(): Int = partecipanti.size
    override fun onBindViewHolder(holder: DestinatarioHolder, position: Int){
        holder.bind(partecipanti[position])
        holder.buttonRimuovi.setOnClickListener { removeItem(position) }
        textView.visibility= View.GONE
    }
    fun removeItem(position: Int) {
        partecipanti.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position,partecipanti.size)
        if(itemCount == 0){
            textView.visibility = View.VISIBLE
            button.isEnabled = false
        }

    }
    class DestinatarioHolder(v: View) : RecyclerView.ViewHolder(v){
        private var view: View = v
        lateinit var buttonRimuovi: Button
        fun bind(partecipante: PartecipanteType) {
            buttonRimuovi = view.buttonRimuoviDestinatario
            view.textViewGeneralita.text = partecipante.generalita
            view.textViewTelefono.text = if(partecipante.numeroTelefono.isNullOrBlank()) view.getResources().getString(
                R.string.not_defined
            ) else partecipante.numeroTelefono
        }

    }
}