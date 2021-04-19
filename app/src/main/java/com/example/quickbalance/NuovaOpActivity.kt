package com.example.quickbalance

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quickbalance.Adapters.PartecipanteAdapter
import com.example.quickbalance.DataTypes.PartecipanteType
import com.example.quickbalance.Utils.FieldUtils
import com.example.quickbalance.Utils.FieldUtils.Companion.controllaNumero
import com.example.quickbalance.Utils.FieldUtils.Companion.normalizzaStringa
import kotlinx.android.synthetic.main.activity_creazione_stepuno.*


class NuovaOpActivity: AppCompatActivity() {
    private lateinit var recyclerViewAdapter:PartecipanteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creazione_stepuno)
        //Listeners vari
        topAppBar.setNavigationOnClickListener(navigationIconOnClickListener)
        buttonAggiungiPartecipante.setOnClickListener(aggiungiPartecipanteOnClickListener)
        buttonImportaRubrica.setOnClickListener(buttonImportaRubricaOnClickListener)
        buttonAvanti.setOnClickListener(buttonAvantiOnClickListener)
        editTextGeneralita.setOnFocusChangeListener(editTextNominativoFocusListener)
        editTextNumero.setOnFocusChangeListener(editTextNumeroFocusListener)
        //RecyclerView
        recyclerViewAdapter = setRecyclerView()
        //Reset dello stato
        if (savedInstanceState != null) {
            editTextGeneralita.setText(savedInstanceState.getString("testoTextViewGeneralita"))
            editTextNumero.setText(savedInstanceState.getString("testoTextViewNumero"))
            recyclerViewAdapter.updateTasks(savedInstanceState.getParcelableArrayList<PartecipanteType>("listaPartecipanti") as ArrayList<PartecipanteType>)
        }
        aggiornaStatoButtonContinua()
    }

    fun aggiornaStatoButtonContinua(){
        if(recyclerViewAdapter.itemCount > 0)
            buttonAvanti.isEnabled = true
        else
            buttonAvanti.isEnabled = false
    }

    private fun setRecyclerView(): PartecipanteAdapter {
        /* Set del recycler view */
        val recyclerViewAdapter = PartecipanteAdapter(arrayListOf(), textViewDatiVuoti, buttonAvanti)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        return recyclerViewAdapter
    }

    private fun aggiungiPartecipante(generalita: String, telefono: String){
        if(recyclerViewAdapter.getList().find{it.generalita.equals(generalita)} == null && recyclerViewAdapter.getList().find{it.numeroTelefono.equals(
                telefono
            )} == null){
            if(generalita.isNotBlank() && controllaNumero(telefono)){
                recyclerViewAdapter.addItem(PartecipanteType(generalita, telefono))
                editTextGeneralita.setText("")
                editTextNumero.setText("")
                editTextGeneralita.requestFocus()
            }
            else
                Toast.makeText(this, getString(R.string.fields_not_valid), Toast.LENGTH_SHORT).show()
        }
        else
            Toast.makeText(this, "$generalita ${getString(R.string.already_exists)}", Toast.LENGTH_SHORT).show()
    }



    val aggiungiPartecipanteOnClickListener=View.OnClickListener {
        aggiungiPartecipante(
            normalizzaStringa(editTextGeneralita.text.toString()),
            editTextNumero.text.toString()
        )
        aggiornaStatoButtonContinua()
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


    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val list:ArrayList<PartecipanteType> = data?.getParcelableArrayListExtra<PartecipanteType>("listaContatti") as ArrayList<PartecipanteType>
            list.forEach{
                val generalita = it.generalita
                val telefono = it.numeroTelefono
                if (generalita != null) {
                    if(generalita.isNotBlank() && recyclerViewAdapter.getList().find{it.generalita.equals(generalita)} == null && recyclerViewAdapter.getList().find{it.numeroTelefono.equals(
                            telefono
                        )} == null)
                        recyclerViewAdapter.addItem(it)
                    else
                        Toast.makeText(this, "$generalita ${getString(R.string.already_exists)}", Toast.LENGTH_SHORT).show()
                }
            }
            aggiornaStatoButtonContinua()
        }
    }

    val buttonImportaRubricaOnClickListener= View.OnClickListener {
        val int: Intent = Intent(this, ImportaContattiActivity::class.java)
        resultLauncher.launch(int)
    }

    val buttonAvantiOnClickListener= View.OnClickListener {
        val int: Intent = Intent(this, OpAggDataActivity::class.java)
        startActivity(int)
    }

    val navigationIconOnClickListener= View.OnClickListener {
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("testoTextViewGeneralita", editTextGeneralita.text.toString())
        outState.putString("testoTextViewNumero", editTextNumero.text.toString())
        outState.putParcelableArrayList("listaPartecipanti", recyclerViewAdapter.getList())
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}