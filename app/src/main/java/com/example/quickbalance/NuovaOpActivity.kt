package com.example.quickbalance

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quickbalance.Adapters.PartecipanteAdapter
import com.example.quickbalance.DataTypes.PartecipanteType
import kotlinx.android.synthetic.main.activity_creazione_stepuno.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class NuovaOpActivity: AppCompatActivity() {
    private lateinit var list:ArrayList<PartecipanteType>
    private lateinit var recyclerViewAdapter:PartecipanteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creazione_stepuno)
        topAppBar.setNavigationOnClickListener(navigationIconOnClickListener)
        buttonAggiungiPartecipante.setOnClickListener(aggiungiPartecipanteOnClickListener)
        recyclerViewAdapter = setRecyclerView()
        list = ArrayList()
    }

    private fun setRecyclerView(): PartecipanteAdapter {
        /* Set del recycler view */
        val recyclerViewAdapter = PartecipanteAdapter(mutableListOf(), textViewDatiVuoti)
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

    private fun aggiungiPartecipante(cognome: String, nome: String, telefono: String){
        if(cognome.isNotBlank() && nome.isNotBlank() && controllaNumero(telefono)){
            list.add(PartecipanteType(cognome, nome, telefono))
            recyclerViewAdapter.addItem(PartecipanteType(cognome, nome, telefono))
        }
        else
            Toast.makeText(this,getString(R.string.fields_not_valid),Toast.LENGTH_SHORT).show()
    }

    private fun normalizzaStringa(sIn:String):String{
        var s = sIn.replace(" ", "")
        s = s.capitalize()
        return s
    }

    val aggiungiPartecipanteOnClickListener=View.OnClickListener {
        aggiungiPartecipante(
            normalizzaStringa(editTextCognome.text.toString()),
            normalizzaStringa(editTextNome.text.toString()),
            editTextNumero.text.toString()
        )
    }

    val navigationIconOnClickListener= View.OnClickListener {
        finish()
    }
}