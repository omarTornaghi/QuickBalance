package com.example.quickbalance

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quickbalance.Adapters.TransazioneStoricoAdapter
import com.example.quickbalance.DataTypes.TransazioneType
import com.example.quickbalance.Database.DbHelper
import kotlinx.android.synthetic.main.activity_storico.*
import kotlinx.android.synthetic.main.activity_storico.topAppBar

class StoricoActivity : AppCompatActivity() {
    private var dbHelper: DbHelper = DbHelper(this)
    private lateinit var transTSpinnerAdapter: ArrayAdapter<CharSequence>
    private lateinit var recyclerViewAdapter: TransazioneStoricoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storico)
        //Adapters spinner
        transTSpinnerAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.spinnerStoricoCompletate,
            R.layout.custom_spinnerstoricocompl_layout
        )
        topAppBar.setNavigationOnClickListener(navigationIconOnClickListener)
        recyclerViewAdapter = TransazioneStoricoAdapter(ArrayList(),textViewNoData)
        recycler_view.adapter = recyclerViewAdapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        transTSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinnercd_dropdown_layout);
        spinnerView.setAdapter(transTSpinnerAdapter)
        if(savedInstanceState != null)  spinnerView.setSelection(savedInstanceState.getInt("spinner")) else spinnerView.setSelection(1)
        spinnerView.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var giorni = 0
                when(spinnerView.selectedItemPosition){
                    1 -> giorni = 7
                    2 -> giorni = 30
                    3 -> giorni = 120
                    4 -> giorni = 360
                    else -> giorni = 0
                }
                recyclerViewAdapter.updateTasks(dbHelper.getTransazioniCompletate(giorni))
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    val navigationIconOnClickListener = View.OnClickListener {
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("spinner", spinnerView.selectedItemPosition)
    }

}