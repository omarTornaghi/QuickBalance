package com.example.quickbalance.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.example.quickbalance.Animations.AnimationUtils
import com.example.quickbalance.DataTypes.CreditType
import com.example.quickbalance.R
import kotlinx.android.synthetic.main.card_crediti.view.*

class CreditAdapter(private val crediti: ArrayList<CreditType>) : RecyclerView.Adapter<CreditAdapter.CreditHolder>(){
    fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditHolder {
        fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
            return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
        }
        val inflatedView = parent.inflate(R.layout.card_crediti, false)
        return CreditHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: CreditAdapter.CreditHolder, position: Int) {
        val itemCredit = crediti[position]
        holder.bind(itemCredit)
    }

    override fun getItemCount() = crediti.size


    class CreditHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private var view: View = v
        private var credito: CreditType? = null
        private var isExpanded = false
        init {
            v.setOnClickListener(this)
        }
        fun bind(credit: CreditType) {
            this.credito = credit
            /* Cambiare i campi */
            view.textViewGeneralita.text = credit.generalita
            view.textViewDescr.text = credit.descrizione
            view.textViewSoldi.text = "€" + credit.soldi.toString()
            view.setOnClickListener {
                if (isExpanded) collapse() else expand()
            }
            //All tasks should be collapsed by default
            collapse()
        }

        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK!")
        }

        private fun expand()
        {
            AnimationUtils.expand(itemView.CL_card_credit)
            //itemView.dropdownButton.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp)
            isExpanded = true
        }
        private fun collapse()
        {
            AnimationUtils.collapse(itemView.CL_card_credit)
            //itemView.dropdownButton.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp)
            isExpanded = false
        }



        companion object {
            private val CREDIT_KEY = "CREDIT"
        }
    }

}