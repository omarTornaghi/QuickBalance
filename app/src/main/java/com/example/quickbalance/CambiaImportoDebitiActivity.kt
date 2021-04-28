package com.example.quickbalance

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.quickbalance.DataTypes.TransazioneType
import kotlinx.android.synthetic.main.activity_cambia_importo_debiti.buttonAzzera
import kotlinx.android.synthetic.main.activity_cambia_importo_debiti.buttonMeta
import kotlinx.android.synthetic.main.activity_cambia_importo_debiti.buttonTotale
import kotlinx.android.synthetic.main.activity_cambia_importo_debiti.editTextImportoTransazione
import kotlinx.android.synthetic.main.activity_cambia_importo_debiti.floatingButtonModificaImporto
import kotlinx.android.synthetic.main.activity_cambia_importo_debiti.textViewImportoTotale
import kotlinx.android.synthetic.main.activity_cambia_importo_debiti.topAppBar
import java.lang.Exception

class CambiaImportoDebitiActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener {
    lateinit var item:TransazioneType
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambia_importo_debiti)
        item = intent.getParcelableExtra<TransazioneType>("item") as TransazioneType
        if(savedInstanceState != null){
            editTextImportoTransazione.setText(savedInstanceState.getString("editTextImportoTransazione"))
        }
        textViewImportoTotale.setText(item.soldiTotali.toString())
        editTextImportoTransazione.setText(item.soldiRicevuti.toString())
        editTextImportoTransazione.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(editTextImportoTransazione.text.toString().isNotBlank())
                    editTextImportoTransazione.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_euro_red_icon,0,R.drawable.ic_baseline_clear_24, 0)
                else
                    editTextImportoTransazione.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_euro_red_icon,0,0, 0)
            }
        })
        editTextImportoTransazione.setOnTouchListener(object : View.OnTouchListener {
            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val DRAWABLE_RIGHT = 2;
                if (event != null) {
                    try {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (event.getRawX() >= (editTextImportoTransazione.getRight() - editTextImportoTransazione.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds()
                                    .width())
                            ) {
                                editTextImportoTransazione.text?.clear()
                                editTextImportoTransazione.requestFocus()
                                return true;
                            }
                        }
                    }
                    catch(ex: Exception){}
                }
                return false;
            }
        })
        buttonAzzera.setOnClickListener{editTextImportoTransazione.setText("0")}
        buttonMeta.setOnClickListener{editTextImportoTransazione.setText("${item.soldiRicevuti + (item.soldiTotali - item.soldiRicevuti)/2}")}
        buttonTotale.setOnClickListener { editTextImportoTransazione.setText(item.soldiTotali.toString()) }
        editTextImportoTransazione.setOnFocusChangeListener(editTextImportoTransazioneFocusListener)
        topAppBar.setOnMenuItemClickListener(this)
        topAppBar.setNavigationOnClickListener(navigationIconOnClickListener)
        floatingButtonModificaImporto.setOnClickListener { cambiaImporto() }
    }

    private val editTextImportoTransazioneFocusListener =
        View.OnFocusChangeListener { view, gainFocus ->
            if (gainFocus) {
                editTextImportoTransazione.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_euro_red_icon,
                    0,
                    0,
                    0
                )
            } else {
                editTextImportoTransazione.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_euro_gray_icon,
                    0,
                    0,
                    0
                )
            }
        }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.checkIcon -> {
                //Controllo i dati
                cambiaImporto()
                return true
            }
        }
        return false;
    }

    fun cambiaImporto(){
        if(editTextImportoTransazione.text.toString().isNotBlank()) {
            val imp = editTextImportoTransazione.text.toString().toDouble()
            if(imp <= item.soldiTotali){
                //TODO QUERY DI AGGIORNAMENTO
                finish()
            }
            else
                Toast.makeText(this, getString(R.string.fields_not_valid), Toast.LENGTH_SHORT).show()
        }
        else
            Toast.makeText(this, getString(R.string.fields_not_valid), Toast.LENGTH_SHORT).show()
    }

    val navigationIconOnClickListener = View.OnClickListener {
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("editTextImportoTransazione", editTextImportoTransazione.text.toString())
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