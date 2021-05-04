package com.example.quickbalance

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quickbalance.Adapters.TransazioneStoricoAdapter
import com.example.quickbalance.Animations.AnimationUtils
import com.example.quickbalance.DataTypes.TransazioneType
import com.example.quickbalance.Database.DbHelper
import kotlinx.android.synthetic.main.activity_storico.*

class StoricoActivity : AppCompatActivity() {
    private var dbHelper: DbHelper = DbHelper(this)
    private var cardTCompletateEspansa: Boolean = true
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
        var list:ArrayList<TransazioneType> = ArrayList()
        list.add(TransazioneType())
        list.add(TransazioneType())
        list.add(TransazioneType())
        recyclerViewAdapter = TransazioneStoricoAdapter(list)
        recycler_view.adapter = recyclerViewAdapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        transTSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinnercd_dropdown_layout);
        spinnerView.setAdapter(transTSpinnerAdapter)
        spinnerView.setSelection(0)

        ecCardTCompletate()

        textViewCardCompletate.setOnClickListener { ecCardTCompletate() }
        buttonColExpCardTCompletate.setOnClickListener { ecCardTCompletate() }
    }

    private fun ecCardTCompletate() {
        if (cardTCompletateEspansa) {
            buttonColExpCardTCompletate.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_baseline_expand_less_blue_dark_24,
                0,
                0,
                0
            )
            AnimationUtils.expand(consLayTCompletate)
        } else {
            buttonColExpCardTCompletate.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_baseline_expand_more_blue_dark_24,
                0,
                0,
                0
            )
            AnimationUtils.collapse(consLayTCompletate)
        }
        cardTCompletateEspansa = !cardTCompletateEspansa
    }
}