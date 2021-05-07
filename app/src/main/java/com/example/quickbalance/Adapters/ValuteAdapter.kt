package com.example.quickbalance.Adapters

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quickbalance.DataTypes.ValutaType
import com.example.quickbalance.R
import com.example.quickbalance.Utils.ValutaUtils
import kotlinx.android.synthetic.main.card_valuta.view.*

class ValuteAdapter(private val valute: ArrayList<ValutaType>, private val codSel:String, private val mListener: ListAdapterListener) : RecyclerView.Adapter<ValuteAdapter.ValutaHolder>(){
    interface ListAdapterListener {
        fun onClickAtRemoveButton(position: Int)
    }

    fun updateTasks(nuovaLista: ArrayList<ValutaType>)
    {
        valute.clear()
        valute.addAll(nuovaLista)
        notifyDataSetChanged()
    }

    fun getItem(position: Int):ValutaType{
        return valute.get(position)
    }

    fun getList():ArrayList<ValutaType>{ return valute}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ValutaHolder
    {
        return ValutaHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_valuta,
                parent,
                false
            )
        )
    }
    override fun getItemCount(): Int = valute.size
    override fun onBindViewHolder(holder: ValutaHolder, position: Int){
        holder.bind(valute[position], codSel)
        holder?.view?.setOnClickListener {
            mListener.onClickAtRemoveButton(position)
        }
    }

    class ValutaHolder(v: View) : RecyclerView.ViewHolder(v){
        var view: View = v
        private lateinit var valuta: ValutaType
        fun bind(valu: ValutaType, codSel:String) {
            valuta = valu
            view.textViewValutaName.text = "${valu.name} (${valu.code})"
            view.textViewValutaSimbolo.text = valu.symbol
            if(codSel == valu.code)
                view.imageViewSelezionata.visibility = View.VISIBLE
            else
                view.imageViewSelezionata.visibility = View.GONE
        }


    }
}