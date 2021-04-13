package com.example.quickbalance

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.activity_creazione_stepuno.*
import kotlinx.android.synthetic.main.activity_op_agg_data.*
import java.text.SimpleDateFormat
import java.util.*


class OpAggDataActivity : AppCompatActivity() {
    var statoToggle:Boolean = false
    val formatoData:String = "dd/MM/yyyy"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_op_agg_data)
        editTextDescrizione.setOnFocusChangeListener(editTextDescrizioneFocusListener)
        editTextimporto.setOnFocusChangeListener(editTextImportoFocusListener)
        toggleButton.setOnClickListener(toggleButtonOnClickListener)
        creditoToggleButton.setOnClickListener{setToggleCredito()}
        debitoToggleButton.setOnClickListener({ setToggleDebito() })
        //Datepicker dataInizio
        val calendar: Calendar = Calendar.getInstance()
        val dataInizio =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateLabelDatePicker(calendar, editTextDataInizio)
            }
        editTextDataInizio.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                editTextDataInizio.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_calendar_green_icon,
                    0,
                    0,
                    0
                )
                val dialog = DatePickerDialog(
                    this@OpAggDataActivity, dataInizio, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                dialog.setButton(
                    DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel)
                ) { dialog, which ->
                    if (which == DialogInterface.BUTTON_NEGATIVE) {
                        editTextDataInizio.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_baseline_calendar_gray_icon,
                            0,
                            0,
                            0
                        )
                    }
                }
                dialog.show()
            }
        })
        //fine datepicker dataInizio
        //datepicker dataScadenza
        val dataScadenza =
            OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateLabelDatePicker(calendar, editTextDataScadenza)
            }
        editTextDataScadenza.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                editTextDataScadenza.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_calendar_green_icon,
                    0,
                    0,
                    0
                )
                val dialog = DatePickerDialog(
                    this@OpAggDataActivity, dataScadenza, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                dialog.setButton(
                    DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel)
                ) { dialog, which ->
                    if (which == DialogInterface.BUTTON_NEGATIVE) {
                        editTextDataScadenza.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_baseline_calendar_gray_icon,
                            0,
                            0,
                            0
                        )
                    }
                }
                dialog.show()
            }
        })
        editTextDataScadenza.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(editTextDataScadenza.text.toString().isNotBlank()){
                    buttonCancellaDataScadenza.visibility = View.VISIBLE
                    val params = editTextDataScadenza.layoutParams as ConstraintLayout.LayoutParams
                    params.rightToLeft = buttonCancellaDataScadenza.id
                    editTextDataScadenza.requestLayout()
                }
                else {
                    buttonCancellaDataScadenza.visibility = View.GONE
                    val params = editTextDataScadenza.layoutParams as ConstraintLayout.LayoutParams
                    params.rightToLeft = consLayDate.id
                    editTextDataScadenza.requestLayout()
                }
            }
        })
        //fine datepicker dataScadenza
        if(savedInstanceState != null){
            statoToggle = savedInstanceState.getBoolean("statoToggle")
            if(statoToggle == true) setToggleCredito() else setToggleDebito()
            editTextDescrizione.setText(savedInstanceState.getString("editTextDescrizione"))
            editTextimporto.setText(savedInstanceState.getString("editTextImporto"))
            editTextDataInizio.setText(savedInstanceState.getString("editTextDataInizio"))
        }
        else {
            //TODO Set toggle in base a chi ha chiamato l'operazione
            setToggleCredito()
            val sdf = SimpleDateFormat(formatoData)
            editTextDataInizio.setText(sdf.format(Date()))
        }
        buttonCancellaDataScadenza.setOnClickListener { editTextDataScadenza.text.clear() }
    }

    fun getCurrentLocale(context: Context): Locale? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.getResources().getConfiguration().getLocales().get(0)
        } else {
            context.getResources().getConfiguration().locale
        }
    }

    private fun updateLabelDatePicker(myCalendar: Calendar, editText: EditText) {
        val sdf = SimpleDateFormat(formatoData, getCurrentLocale(this))
        editText.setText(sdf.format(myCalendar.getTime()))
        editText.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_baseline_calendar_gray_icon,
            0,
            0,
            0
        )
    }

    private val toggleButtonOnClickListener = View.OnClickListener { view ->
        if(creditoToggleButton.isChecked)
            setToggleDebito()
        else
            setToggleCredito()
    }

    private fun setToggleCredito(){
        toggleButton.setBackgroundResource(R.drawable.background_switch_green)
        creditoToggleButton.setTextColor(
            ResourcesCompat.getColor(
                getResources(),
                R.color.green,
                null
            )
        )
        debitoToggleButton.setTextColor(
            ResourcesCompat.getColor(
                getResources(),
                R.color.white,
                null
            )
        )
        creditoToggleButton.isChecked = true
        statoToggle = true
    }

    private fun setToggleDebito(){
        toggleButton.setBackgroundResource(R.drawable.background_switch_red)
        creditoToggleButton.setTextColor(
            ResourcesCompat.getColor(
                getResources(),
                R.color.white,
                null
            )
        )
        debitoToggleButton.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null))
        debitoToggleButton.isChecked = true
        statoToggle = false
    }

    private val editTextDescrizioneFocusListener =
        View.OnFocusChangeListener { view, gainFocus ->
            if (gainFocus) {
                editTextDescrizione.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_description_green_icon,
                    0,
                    0,
                    0
                )
            } else {
                editTextDescrizione.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_description_gray_icon,
                    0,
                    0,
                    0
                )
            }
        }
    private val editTextImportoFocusListener =
        View.OnFocusChangeListener { view, gainFocus ->
            if (gainFocus) {
                editTextimporto.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_euro_green_icon,
                    0,
                    0,
                    0
                )
            } else {
                editTextimporto.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_euro_gray_icon,
                    0,
                    0,
                    0
                )
            }
        }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("statoToggle", statoToggle)
        outState.putString("editTextDescrizione", editTextDescrizione.text.toString())
        outState.putString("editTextImporto", editTextimporto.text.toString())
        outState.putString("editTextDataInizio", editTextDataInizio.text.toString())
    }
}