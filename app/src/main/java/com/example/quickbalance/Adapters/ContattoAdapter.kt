package com.example.quickbalance.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quickbalance.DataTypes.ContattoType
import com.example.quickbalance.R
import kotlinx.android.synthetic.main.card_contatto.view.*

class ContattoAdapter(private val contatti: ArrayList<ContattoType>, private var textView:TextView,val clickListener: (ContattoType) -> Unit) : RecyclerView.Adapter<ContattoAdapter.ContattoHolder>(){
    fun updateTasks(nuovaLista: ArrayList<ContattoType>)
    {
        contatti.clear()
        contatti.addAll(nuovaLista)
        notifyDataSetChanged()
    }

    fun addItem(item:ContattoType){
        contatti.add(item)
        notifyDataSetChanged()
    }

    fun setItem(itemOld:ContattoType, itemNew:ContattoType){
        val id = contatti.indexOf(itemOld)
        contatti.set(id, itemNew)
        notifyDataSetChanged()
    }

    fun getList(): ArrayList<ContattoType> { return contatti}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContattoHolder
    {
        return ContattoHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_contatto,
                parent,
                false
            )
        )
    }
    override fun getItemCount(): Int = contatti.size
    override fun onBindViewHolder(holder: ContattoHolder, position: Int){
        holder.bind(contatti[position])
        holder?.view?.setOnClickListener { clickListener(contatti[position]) }
        holder?.view?.checkSelezionato.setOnClickListener{ clickListener(contatti[position]) }
        textView.visibility= View.GONE
    }

    class ContattoHolder(v: View) : RecyclerView.ViewHolder(v){
        var view: View = v
        private lateinit var contact: ContattoType
        fun bind(contatto: ContattoType) {
            contact = contatto
            view.textViewNomeContatto.text = if(contatto.generalita.isNullOrBlank()) view.getResources().getString(
                R.string.not_defined
            ) else contatto.generalita
            view.textViewNumeroContatto.text = if(contatto.numeroTelefono.isNullOrBlank()) view.getResources().getString(
                R.string.not_defined
            ) else contatto.numeroTelefono
            if(contatto.selezionato == true)
                view.checkSelezionato.isChecked = true
            else
                view.checkSelezionato.isChecked = false
        }


    }
}