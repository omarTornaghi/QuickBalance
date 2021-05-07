package com.example.quickbalance

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quickbalance.Adapters.ValuteAdapter
import com.example.quickbalance.DataTypes.ValutaType
import com.example.quickbalance.Utils.JSONUtils.Companion.getJsonDataFromAsset
import com.example.quickbalance.Utils.ValutaUtils
import kotlinx.android.synthetic.main.activity_cambia_valuta.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CambiaValutaActivity : AppCompatActivity() {
    private lateinit var recyclerViewAdapter: ValuteAdapter
    private var listValute: ArrayList<ValutaType> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambia_valuta)
        topAppBar.setNavigationOnClickListener{finish()}

        val jsonFileString = getJsonDataFromAsset(applicationContext, "currency.json")
        val gson = Gson()
        val listValutaType = object : TypeToken<List<ValutaType>>() {}.type
        listValute = gson.fromJson(jsonFileString, listValutaType)
        recyclerViewAdapter = ValuteAdapter(listValute, ValutaUtils.getSelectedCurrencyCode(this@CambiaValutaActivity),
            object : ValuteAdapter.ListAdapterListener {
                override fun onClickAtRemoveButton(position: Int) {
                    ValutaUtils.saveCurrencyCode(this@CambiaValutaActivity, recyclerViewAdapter.getItem(position).code)
                    finish()
                }
            })

        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        editTextRicercaValuta.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                editTextRicercaValuta.clearFocus()
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(
                    editTextRicercaValuta.getWindowToken(),
                    InputMethodManager.RESULT_UNCHANGED_SHOWN
                )
                ricerca()
                true
            }
            false
        }


        editTextRicercaValuta.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                ricerca()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(editTextRicercaValuta.text.toString().isNotBlank())
                    editTextRicercaValuta.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_icon,0,R.drawable.ic_baseline_clear_24, 0)
                else
                    editTextRicercaValuta.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_icon,0,0, 0)
            }
        })


        editTextRicercaValuta.setOnTouchListener(object : View.OnTouchListener {
            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val DRAWABLE_RIGHT = 2;
                if (event != null) {
                    try {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (event.getRawX() >= (editTextRicercaValuta.getRight() - editTextRicercaValuta.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds()
                                    .width())
                            ) {
                                editTextRicercaValuta.text.clear()
                                editTextRicercaValuta.requestFocus()
                                return true;
                            }
                        }
                    }
                    catch(ex:Exception){}
                }
                return false;
            }
        })

    }

    fun ricerca(){
        if (editTextRicercaValuta.text.isNotBlank()) {
            var listaRicerca: ArrayList<ValutaType> =
                ArrayList(listValute.filter { it ->
                    it.name.toLowerCase().contains(editTextRicercaValuta.text.toString().toLowerCase())
                })
            recyclerViewAdapter.updateTasks(listaRicerca)
        }
        else{
            val jsonFileString = getJsonDataFromAsset(applicationContext, "currency.json")
            val gson = Gson()
            val listValutaType = object : TypeToken<List<ValutaType>>() {}.type
            listValute = gson.fromJson(jsonFileString, listValutaType)
            recyclerViewAdapter.updateTasks(listValute)
        }

    }

}