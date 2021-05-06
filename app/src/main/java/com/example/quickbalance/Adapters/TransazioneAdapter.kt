package com.example.quickbalance.Adapters

import android.app.Activity
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
import com.example.quickbalance.Utils.ValutaUtils
import kotlinx.android.synthetic.main.card_crediti.view.*


class TransazioneAdapter(private val transazioni: MutableList<TransazioneType>, private val mListener:ListAdapterListener, private val activity: Activity) : RecyclerView.Adapter<TransazioneAdapter.TransazioneHolder>(){
    interface ListAdapterListener {
        fun onClickAtRemoveButton(position: Int)
    }

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
        return if (trans.credito) R.layout.card_crediti else R.layout.card_debiti
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransazioneHolder
    {
        val itemView: View
        if (viewType == R.layout.card_crediti) {
            itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.card_crediti,
                parent,
                false
            )
        } else {
            itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.card_debiti,
                parent,
                false
            )
        }
        return TransazioneHolder(itemView, activity)
    }
    override fun getItemCount(): Int = transazioni.size
    override fun onBindViewHolder(holder: TransazioneHolder, position: Int){
        holder.bind(transazioni[position])
        holder.buttonRimuovi.setOnClickListener { mListener.onClickAtRemoveButton(position) }
    }

    class TransazioneHolder(v: View, val activity: Activity) : RecyclerView.ViewHolder(v){
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
            view.textViewCreditoGeneralita.text = if(transazione.generalita.isNullOrBlank()) view.getResources().getString(R.string.not_defined).capitalize() else transazione.generalita
            view.textViewCreditoDescr.text = if(transazione.descrizione.isNullOrBlank()) view.getResources().getString(R.string.not_defined).capitalize() else transazione.descrizione
            view.textViewCreditoSoldiRimanenti.text = ValutaUtils.getSelectedCurrencySymbol(activity) + " "+(transazione.soldiTotali - transazione.soldiRicevuti).toString()
            view.textViewCreditoSoldiRicevuti.text = ValutaUtils.getSelectedCurrencySymbol(activity) + " "+ transazione.soldiRicevuti
            view.textViewCreditoSoldiTotali.text = ValutaUtils.getSelectedCurrencySymbol(activity) + " "+ transazione.soldiTotali
            view.textViewCreditoTelefono.text = if(transazione.numeroTelefono.isNullOrBlank()) view.getResources().getString(R.string.not_defined).capitalize() else transazione.numeroTelefono
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
            var int: Intent
            if(item.credito)
                int = Intent(view.context, CambiaImportoCreditiActivity::class.java)
            else
                int = Intent(view.context, CambiaImportoDebitiActivity::class.java)
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