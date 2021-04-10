package com.example.quickbalance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.activity_op_agg_data.*

class OpAggDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_op_agg_data)
        editTextDescrizione.setOnFocusChangeListener(editTextDescrizioneFocusListener)
        editTextimporto.setOnFocusChangeListener(editTextImportoFocusListener)
        toggleButton.setOnClickListener(toggleButtonOnClickListener)
        creditoToggleButton.setOnClickListener{setToggleCredito()}
        debitoToggleButton.setOnClickListener({setToggleDebito()})
        //TODO Set toggle in base a chi ha chiamato l'operazione
        setToggleDebito()
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
    }

    private fun setToggleDebito(){
        toggleButton.setBackgroundResource(R.drawable.background_switch_red)
        creditoToggleButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.white, null))
        debitoToggleButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null))
        debitoToggleButton.isChecked = true
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
}