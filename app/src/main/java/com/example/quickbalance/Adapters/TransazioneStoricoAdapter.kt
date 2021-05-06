package com.example.quickbalance.Adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quickbalance.DataTypes.TransazioneType
import com.example.quickbalance.DataTypes.ValutaType
import com.example.quickbalance.R
import com.example.quickbalance.Utils.ValutaUtils
import kotlinx.android.synthetic.main.card_storico_t_compl_crediti.view.*

class TransazioneStoricoAdapter(private val transazioni: MutableList<TransazioneType>, private val textViewNoData: TextView, private val activity:Activity) : RecyclerView.Adapter<TransazioneStoricoAdapter.TransazioneStoricoHolder>(){
    fun updateTasks(nuovoTransazione: List<TransazioneType>)
    {
        transazioni.clear()
        transazioni.addAll(nuovoTransazione)
        if(transazioni.size > 0) textViewNoData.visibility = View.GONE else textViewNoData.visibility = View.VISIBLE
        notifyDataSetChanged()
    }
    override fun getItemViewType(position: Int): Int {
        val trans = transazioni.get(position)
        return if (trans.credito) R.layout.card_storico_t_compl_crediti else R.layout.card_storico_t_compl_debiti
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransazioneStoricoHolder
    {
        val itemView: View
        if (viewType == R.layout.card_storico_t_compl_crediti) {
            itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.card_storico_t_compl_crediti,
                parent,
                false
            )
        } else {
            itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.card_storico_t_compl_debiti,
                parent,
                false
            )
        }
        return TransazioneStoricoHolder(itemView, activity)
    }
    override fun getItemCount(): Int = transazioni.size
    override fun onBindViewHolder(storicoHolder: TransazioneStoricoHolder, position: Int){
        storicoHolder.bind(transazioni[position])
    }

    class TransazioneStoricoHolder(v: View, val activity:Activity) : RecyclerView.ViewHolder(v){
        private var view: View = v
        private lateinit var item:TransazioneType
        fun bind(transazione: TransazioneType) {
            item = transazione
            view.textViewGen.text = if(transazione.generalita.isNullOrBlank()) view.getResources().getString(R.string.not_defined).capitalize() else transazione.generalita
            view.textViewDescrizione.text = if(transazione.descrizione.isNullOrBlank()) view.getResources().getString(R.string.not_defined).capitalize() else transazione.descrizione
            view.textViewImporto.text = ValutaUtils.getSelectedCurrencySymbol(activity) + " " + transazione.soldiTotali.toString()
            view.textViewData.text = transazione.dataEstinzione
        }
    }

}