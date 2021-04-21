package com.example.quickbalance.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.quickbalance.Animations.AnimationUtils
import com.example.quickbalance.DataTypes.CreditType
import com.example.quickbalance.OpAggDataActivity
import com.example.quickbalance.R
import kotlinx.android.synthetic.main.card_crediti.view.*


class CreditAdapter(private val crediti: MutableList<CreditType>, private val mListener:ListAdapterListener) : RecyclerView.Adapter<CreditAdapter.CreditHolder>(){
    interface ListAdapterListener {
        // create an interface
        fun onClickAtRemoveButton(position: Int)
    }

    fun updateTasks(nuovoCredit: List<CreditType>)
    {
        crediti.clear()
        crediti.addAll(nuovoCredit)
        notifyDataSetChanged()
    }
    fun removeItem(position: Int){
        crediti.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, crediti.size)
    }
    fun getItem(position:Int):CreditType{
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
        private lateinit var item:CreditType
        lateinit var buttonRimuovi: Button
        fun bind(credit: CreditType) {
            item = credit
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
            view.textViewCreditoGeneralita.text = credit.generalita
            view.textViewCreditoDescr.text = credit.descrizione
            view.textViewCreditoSoldiRimanenti.text = "€" + (credit.soldiTotali - credit.soldiRicevuti).toString()
            view.textViewCreditoSoldiRicevuti.text = "€" + credit.soldiRicevuti
            view.textViewCreditoSoldiTotali.text = "€" + credit.soldiTotali
            view.textViewCreditoTelefono.text = credit.numeroTelefono
            view.textViewCreditoDataInizio.text = credit.dataInizio
            val dataFine: String = if(credit.dataFine == null) "Non definito" else credit.dataFine.toString()
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

        }

        val buttonModificaOnClickListener = View.OnClickListener {
            val int:Intent = Intent(view.context, OpAggDataActivity::class.java)
            int.putExtra("activityModifica", true)
            int.putExtra("item", item)
            startActivity(view.context, int, null)
        }

    }

}