package com.example.quickbalance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_op_agg_data.*
import kotlinx.android.synthetic.main.activity_op_agg_data.spinnerView

class OpAggDataActivity : AppCompatActivity() {
    lateinit var cdSpinnerAdapter: ArrayAdapter<CharSequence>
    var setSpinner:Boolean = false
    var posSelez:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_op_agg_data)
        cdSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinnerData, R.layout.custom_spinner_edittext_green_layout)
        cdSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinnercd_dropdown_layout);
        spinnerView.setAdapter(cdSpinnerAdapter)
        editTextDescrizione.setOnFocusChangeListener(editTextDescrizioneFocusListener)
        editTextimporto.setOnFocusChangeListener(editTextImportoFocusListener)
        spinnerView.setOnTouchListener(spinnerOnTouchListener)
        spinnerView.setOnKeyListener(spinnerOnKeyListener)
        spinnerView.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("XXXX", "SetSPinner: " + setSpinner)
                if(setSpinner){
                    if(position == 0)
                        cdSpinnerAdapter = ArrayAdapter.createFromResource(this@OpAggDataActivity, R.array.spinnerData, R.layout.custom_spinner_edittext_green_layout)
                    else
                        cdSpinnerAdapter = ArrayAdapter.createFromResource(this@OpAggDataActivity, R.array.spinnerData, R.layout.custom_spinner_edittext_red_layout)
                    cdSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinnercd_dropdown_layout)
                    Log.d("XXXX", "Posizione considerata: " + position)
                    Log.d("XXXX", "Posizione salvata: " + posSelez)
                    if(posSelez != position) {
                        setSpinner = false
                        spinnerView.setSelection(position, false)
                        posSelez = position;
                        spinnerView.setAdapter(cdSpinnerAdapter)
                    }
                }
                else
                    spinnerView.setSelection(posSelez, true)
                setSpinner = false

            }

            }
    }

    private val spinnerOnTouchListener = View.OnTouchListener{ view, event ->
        setSpinner = true;
        false
    }

    private val spinnerOnKeyListener = View.OnKeyListener{ view: View, i: Int, keyEvent: KeyEvent ->
        setSpinner = true
        false
    }

    private fun setSpinner(res:Int) {
        /* Set dello spinner */
        cdSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.spinnerData, res)
        cdSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinnercd_dropdown_layout);
        spinnerView.setAdapter(cdSpinnerAdapter)
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