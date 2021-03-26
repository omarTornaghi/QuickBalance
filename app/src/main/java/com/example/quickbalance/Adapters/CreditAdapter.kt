package com.example.quickbalance.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quickbalance.Animations.AnimationUtils
import com.example.quickbalance.DataTypes.CreditType
import com.example.quickbalance.R
import kotlinx.android.synthetic.main.card_crediti.view.*

class CreditAdapter(private val crediti: MutableList<CreditType>) : RecyclerView.Adapter<CreditAdapter.CreditHolder>(){
    fun updateTasks(nuovoCredit: List<CreditType>)
    {
        crediti.clear()
        crediti.addAll(nuovoCredit)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditHolder
    {
        return CreditHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_crediti, parent, false))
    }
    override fun getItemCount(): Int = crediti.size
    override fun onBindViewHolder(holder: CreditHolder, position: Int) = holder.bind(crediti[position])

    class CreditHolder(v: View) : RecyclerView.ViewHolder(v){
        private var view: View = v
        private var isExpanded = false

        fun bind(credit: CreditType) {
            /* Cambiare i campi */
            view.textViewCreditoGeneralita.text = credit.generalita
            view.textViewCreditoDescr.text = credit.descrizione
            view.textViewCreditoSoldiRimanenti.text = "€" + (credit.soldiTotali - credit.soldiRicevuti).toString()
            view.textViewCreditoSoldiRicevuti.text = "€" + credit.soldiRicevuti
            view.textViewCreditoSoldiTotali.text = "€" + credit.soldiTotali
            view.textViewCreditoTelefono.text = credit.numeroTelefono
            view.textViewCreditoDataInizio.text = credit.dataInizio
            val dataFine: String = if(credit.dataFine == null) "Non definito" else credit.dataFine.toString()
            view.textViewCreditoDataFine.text = dataFine
            view.setOnClickListener {
                if (isExpanded) collapse() else expand()
            }
            if(isExpanded == false){
                itemView.CL_card_credit.visibility = View.GONE
                itemView.buttonRiscatta1.visibility = View.GONE
                collapse()
            }
        }

        private fun expand()
        {
            AnimationUtils.expand(itemView.CL_card_credit)
            itemView.buttonRiscatta1.visibility = View.GONE
            itemView.buttonCancellaCredito.visibility = View.VISIBLE
            isExpanded = true
        }
        private fun collapse()
        {
            AnimationUtils.collapse(itemView.CL_card_credit)
            itemView.buttonRiscatta1.visibility = View.VISIBLE
            itemView.buttonCancellaCredito.visibility = View.GONE
            isExpanded = false
        }
    }

}