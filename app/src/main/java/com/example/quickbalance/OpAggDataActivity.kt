package com.example.quickbalance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.activity_creazione_stepuno.*
import kotlinx.android.synthetic.main.activity_op_agg_data.*

class OpAggDataActivity : AppCompatActivity() {
    var statoToggle:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_op_agg_data)
        editTextDescrizione.setOnFocusChangeListener(editTextDescrizioneFocusListener)
        editTextimporto.setOnFocusChangeListener(editTextImportoFocusListener)
        toggleButton.setOnClickListener(toggleButtonOnClickListener)
        creditoToggleButton.setOnClickListener{setToggleCredito()}
        debitoToggleButton.setOnClickListener({setToggleDebito()})
        if(savedInstanceState != null){
            statoToggle = savedInstanceState.getBoolean("statoToggle")
            if(statoToggle == true) setToggleCredito() else setToggleDebito()
            editTextDescrizione.setText(savedInstanceState.getString("editTextDescrizione"))
            editTextimporto.setText(savedInstanceState.getString("editTextImporto"))
        }
        else {
            //TODO Set toggle in base a chi ha chiamato l'operazione
            setToggleCredito()
        }
    }

    private val toggleButtonOnClickListener = View.OnClickListener { view ->
        if(creditoToggleButton.isChecked)
            setToggleDebito()
        else
            setToggleCredito()
    }

    private fun setToggleCredito(){
        toggleButton.setBackgroundResource(R.drawable.background_switch_green)
        creditoToggleButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.green, null))
        debitoToggleButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null))
        creditoToggleButton.isChecked = true
        statoToggle = true
    }

    private fun setToggleDebito(){
        toggleButton.setBackgroundResource(R.drawable.background_switch_red)
        creditoToggleButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null))
        debitoToggleButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null))
        debitoToggleButton.isChecked = true
        statoToggle = false
    }


    private val editTextDescrizioneFocusListener =
        View.OnFocusChangeListener { view, gainFocus ->
            if (gainFocus) {
                editTextDescrizione.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_description_green_icon, 0, 0, 0)
            } else {
                editTextDescrizione.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_description_gray_icon, 0, 0, 0)
            }
        }
    private val editTextImportoFocusListener =
        View.OnFocusChangeListener { view, gainFocus ->
            if (gainFocus) {
                editTextimporto.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_euro_green_icon, 0, 0, 0)
            } else {
                editTextimporto.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_euro_gray_icon, 0, 0, 0)
            }
        }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("statoToggle", statoToggle)
        outState.putString("editTextDescrizione", editTextDescrizione.text.toString())
        outState.putString("editTextImporto", editTextimporto.text.toString())
    }
}