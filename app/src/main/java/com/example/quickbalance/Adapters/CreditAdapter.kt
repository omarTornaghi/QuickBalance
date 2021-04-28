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
import com.example.quickbalance.DataTypes.TransazioneType
import com.example.quickbalance.OpAggDataActivity
import com.example.quickbalance.R
import kotlinx.android.synthetic.main.card_crediti.view.*


class CreditAdapter(private val crediti: MutableList<TransazioneType>, private val mListener:ListAdapterListener) : RecyclerView.Adapter<CreditAdapter.CreditHolder>(){
    interface ListAdapterListener {
        // create an interface
        fun onClickAtRemoveButton(position: Int)
    }

    fun updateTasks(nuovoTransazione: List<TransazioneType>)
    {
        crediti.clear()
        crediti.addAll(nuovoTransazione)
        notifyDataSetChanged()
    }
    fun removeItem(position: Int){
        crediti.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, crediti.size)
    }
    fun getItem(position:Int):TransazioneType{
        return crediti.get(position)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditHolder
    {
        return CreditHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_crediti,
                parent,
                false
            )
        )
    }
    override fun getItemCount(): Int = crediti.size
    override fun onBindViewHolder(holder: CreditHolder, position: Int){
        holder.bind(crediti[position])
        holder.buttonRimuovi.setOnClickListener { mListener.onClickAtRemoveButton(position) }
    }

    class CreditHolder(v: View) : RecyclerView.ViewHolder(v){
        private var view: View = v
        private lateinit var item:TransazioneType
        lateinit var buttonRimuovi: Button
        fun bind(transazione: TransazioneType) {
            item = transazione
            if(item.espanso == false){
                view.CL_card_credit.visibility = View.GONE
                collapse()
            }
            else
            {
                view.CL_card_credit.visibility = View.VISIBLE
                expand()
            }
            buttonRimuovi = view.buttonCancellaCredito
            /* Cambiare i campi */
            view.textViewCreditoGeneralita.text = transazione.generalita
            view.textViewCreditoDescr.text = transazione.descrizione
            view.textViewCreditoSoldiRimanenti.text = "€" + (transazione.soldiTotali - transazione.soldiRicevuti).toString()
            view.textViewCreditoSoldiRicevuti.text = "€" + transazione.soldiRicevuti
            view.textViewCreditoSoldiTotali.text = "€" + transazione.soldiTotali
            view.textViewCreditoTelefono.text = transazione.numeroTelefono
            view.textViewCreditoDataInizio.text = transazione.dataInizio
            val dataFine: String = if(transazione.dataFine.isNullOrBlank()) view.getResources().getString(R.string.not_defined) else transazione.dataFine.toString()
            view.textViewCreditoDataFine.text = dataFine

            //Set dei listener
            view.setOnClickListener(cardOnClickListener)
            view.buttonRiscatta1.setOnClickListener(buttonRiscattaOnClickListener)
            view.buttonRiscatta2.setOnClickListener(buttonRiscattaOnClickListener)
            view.buttonModificaCredito.setOnClickListener(buttonModificaOnClickListener)

        }

        private fun expand()
        {
            view.buttonRiscatta1.visibility = View.GONE
            view.buttonCancellaCredito.visibility = View.VISIBLE
            view.CL_card_credit.visibility = View.VISIBLE
            AnimationUtils.expand(view.CL_card_credit)
            item.espanso = true
        }
        private fun collapse()
        {
            AnimationUtils.collapse(view.CL_card_credit)
            view.buttonRiscatta1.visibility = View.VISIBLE
            view.buttonCancellaCredito.visibility = View.GONE
            item.espanso = false
        }

        val cardOnClickListener= View.OnClickListener {
            if (item.espanso) collapse() else expand()
        }

        val buttonRiscattaOnClickListener= View.OnClickListener {
            val int = Intent(view.context, CambiaImportoCreditiActivity::class.java)
            int.putExtra("item", item)
            startActivity(view.context, int,null)
        }

        val buttonModificaOnClickListener = View.OnClickListener {
            val int:Intent = Intent(view.context, OpAggDataActivity::class.java)
            int.putExtra("activityModifica", true)
            int.putExtra("item", item)
            startActivity(view.context, int, null)
        }

    }

}