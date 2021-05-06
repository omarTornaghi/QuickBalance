package com.example.quickbalance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quickbalance.Adapters.TransazioneAdapter
import com.example.quickbalance.Adapters.ValuteAdapter
import com.example.quickbalance.DataTypes.ValutaType
import com.example.quickbalance.Utils.JSONUtils.Companion.getJsonDataFromAsset
import com.example.quickbalance.Utils.ValutaUtils
import kotlinx.android.synthetic.main.activity_cambia_valuta.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CambiaValutaActivity : AppCompatActivity() {
    private lateinit var recyclerViewAdapter: ValuteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambia_valuta)
        topAppBar.setNavigationOnClickListener{finish()}

        val jsonFileString = getJsonDataFromAsset(applicationContext, "currency.json")
        val gson = Gson()
        val listValutaType = object : TypeToken<List<ValutaType>>() {}.type
        var valute: List<ValutaType> = gson.fromJson(jsonFileString, listValutaType)
        recyclerViewAdapter = ValuteAdapter(valute as ArrayList<ValutaType>, ValutaUtils.getSelectedCurrencyCode(this@CambiaValutaActivity),
            object : ValuteAdapter.ListAdapterListener {
                override fun onClickAtRemoveButton(position: Int) {
                    ValutaUtils.saveCurrencyCode(this@CambiaValutaActivity, recyclerViewAdapter.getItem(position).code)
                    finish()
                }
            })

        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

    }
}