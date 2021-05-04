package com.example.quickbalance.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.quickbalance.Animations.AnimationUtils
import com.example.quickbalance.CambiaImportoCreditiActivity
import com.example.quickbalance.CambiaImportoDebitiActivity
import com.example.quickbalance.DataTypes.TransazioneType
import com.example.quickbalance.OpAggDataActivity
import com.example.quickbalance.R
import kotlinx.android.synthetic.main.card_crediti.view.*


class TransazioneStoricoAdapter(private val transazioni: MutableList<TransazioneType>) : RecyclerView.Adapter<TransazioneStoricoAdapter.TransazioneStoricoHolder>(){
    fun updateTasks(nuovoTransazione: List<TransazioneType>)
    {
        transazioni.clear()
        transazioni.addAll(nuovoTransazione)
        notifyDataSetChanged()
    }
    fun removeItem(position: Int){
        transazioni.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, transazioni.size)
    }
    fun getItem(position:Int):TransazioneType{
        return transazioni.get(position)
    }

    override fun getItemViewType(position: Int): Int {
        val trans = transazioni.get(position)
        return R.layout.card_storico_t_compl
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransazioneStoricoHolder
    {
        val itemView: View = LayoutInflater.from(parent.context).inflate(
            R.layout.card_storico_t_compl,
            parent,
            false
        )
        return TransazioneStoricoHolder(itemView)
    }
    override fun getItemCount(): Int = transazioni.size
    override fun onBindViewHolder(storicoHolder: TransazioneStoricoHolder, position: Int){
        storicoHolder.bind(transazioni[position])

    }

    class TransazioneStoricoHolder(v: View) : RecyclerView.ViewHolder(v){
        private var view: View = v
        private lateinit var item:TransazioneType
        fun bind(transazione: TransazioneType) {
            item = transazione

        }
    }

}