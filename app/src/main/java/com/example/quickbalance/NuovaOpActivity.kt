package com.example.quickbalance

import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
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
        editTextGeneralita.setOnFocusChangeListener(editTextNominativoFocusListener)
        editTextNumero.setOnFocusChangeListener(editTextNumeroFocusListener)
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

    private fun aggiungiPartecipante(generalita: String, telefono: String){
        if(list.find{it.generalita.equals(generalita)} == null && list.find{it.numeroTelefono.equals(
                telefono
            )} == null){
            if(generalita.isNotBlank() && controllaNumero(telefono)){
                list.add(PartecipanteType(generalita, telefono))
                recyclerViewAdapter.addItem(PartecipanteType(generalita, telefono))
            }
            else
                Toast.makeText(this, getString(R.string.fields_not_valid), Toast.LENGTH_SHORT).show()
        }
        else
            Toast.makeText(this, getString(R.string.member_already_exists), Toast.LENGTH_SHORT).show()
    }

    private fun normalizzaStringa(sIn: String):String{
        return sIn.trim().capitalize()
    }

    val aggiungiPartecipanteOnClickListener=View.OnClickListener {
        aggiungiPartecipante(
            normalizzaStringa(editTextGeneralita.text.toString()),
            editTextNumero.text.toString()
        )
    }

    private val editTextNominativoFocusListener =
        OnFocusChangeListener { view, gainFocus ->
            if (gainFocus) {
                editTextGeneralita.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_identity_green_icon, 0, 0, 0)
            } else {
                editTextGeneralita.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_identity_gray_icon, 0, 0, 0)
            }
        }
    private val editTextNumeroFocusListener =
        OnFocusChangeListener { view, gainFocus ->
            if (gainFocus) {
                editTextNumero.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_smartphone_green_icon, 0, 0, 0)
            } else {
                editTextNumero.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_smartphone_gray_icon, 0, 0, 0)
            }
        }
    val navigationIconOnClickListener= View.OnClickListener {
        finish()
    }
}