package com.example.quickbalance

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quickbalance.Adapters.DestinatarioAdapter
import com.example.quickbalance.DataTypes.DestinatarioType
import kotlinx.android.synthetic.main.activity_creazione_stepuno.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class NuovaOpActivity: AppCompatActivity() {
    private lateinit var list:ArrayList<DestinatarioType>
    private lateinit var recyclerViewAdapter:DestinatarioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creazione_stepuno)
        topAppBar.setNavigationOnClickListener(navigationIconOnClickListener)
        buttonAggiungiDestinatario.setOnClickListener(aggiungiDestinatarioOnClickListener)
        recyclerViewAdapter = setRecyclerView()
        list = ArrayList()
    }

    private fun setRecyclerView(): DestinatarioAdapter {
        /* Set del recycler view */
        val recyclerViewAdapter = DestinatarioAdapter(mutableListOf(), textViewDatiVuoti)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        return recyclerViewAdapter
    }

    private fun controllaNumero(telefono: String):Boolean{
        val m:Matcher
        val pattern = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$"
        val r: Pattern = Pattern.compile(pattern)
        m = r.matcher(telefono.trim())
        if ((telefono.isBlank() || m.find())){
            if(telefono.isBlank() || (telefono.get(0) != '+' && telefono.length == 10))
                return true
            if(telefono.isBlank() || (telefono.get(0) == '+' && telefono.length == 13))
                return true
        }
        return false
    }

    private fun aggiungiDestinatario(cognome: String, nome: String, telefono: String){
        if(cognome.isNotBlank() && nome.isNotBlank() && controllaNumero(telefono)){
            list.add(DestinatarioType(cognome, nome, telefono))
            recyclerViewAdapter.addItem(DestinatarioType(cognome, nome, telefono))
        }
        else
            Toast.makeText(this,getString(R.string.fields_not_valid),Toast.LENGTH_SHORT).show()
    }

    private fun normalizzaStringa(sIn:String):String{
        var s = sIn.replace(" ", "")
        s = s.capitalize()
        return s
    }

    val aggiungiDestinatarioOnClickListener=View.OnClickListener {
        aggiungiDestinatario(
            normalizzaStringa(editTextCognome.text.toString()),
            normalizzaStringa(editTextNome.text.toString()),
            editTextNumero.text.toString()
        )
    }

    val navigationIconOnClickListener= View.OnClickListener {
        finish()
    }
}