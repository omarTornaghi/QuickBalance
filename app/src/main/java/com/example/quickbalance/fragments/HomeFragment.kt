package com.example.quickbalance.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.quickbalance.Database.DbHelper
import com.example.quickbalance.R
import com.example.quickbalance.StoricoActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    private lateinit var mContext: Context
    private lateinit var dbHelper: DbHelper
    private var totCrediti:Double = 0.00
    private var totDebiti:Double = 0.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        dbHelper = DbHelper(mContext)
        totCrediti = dbHelper.getTotalImportCredit()
        totDebiti = dbHelper.getTotalImportDebit()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cardStorico.setOnClickListener(cardOnClickListener)
        inizializzaGrafico()
        if(totCrediti==0.00 && totDebiti==0.00)
            pieChart_graph.visibility = View.GONE
        else
            textViewNodata.visibility = View.GONE
        popolaGrafico()
        textViewCrediti.setText("€$totCrediti")
        textViewDebiti.setText("€$totDebiti")
        val daPag:Double = if(totDebiti >= totCrediti) totDebiti - totCrediti else 0.00
        textViewDaPagare.setText("€$daPag")
        if(daPag == 0.00) {
            textViewDaPag.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_smile, 0, 0, 0)
            textViewDaPagare.setTextColor(ContextCompat.getColor(mContext, R.color.green))
        }
        else {
            textViewDaPag.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sad, 0, 0, 0)
            textViewDaPagare.setTextColor(ContextCompat.getColor(mContext, R.color.red))
        }
    }

    private fun inizializzaGrafico() {
        //utilizzo percentuale
        pieChart_graph.setUsePercentValues(true)
        //elimino label descrittiva
        pieChart_graph.description.isEnabled = false
        //rotazione grafico
        pieChart_graph.isRotationEnabled = true
        //frizione rotazione
        pieChart_graph.dragDecelerationFrictionCoef = 0.9f
        //setto il primo dato lato destro
        pieChart_graph.rotationAngle = 0f
        //Set della legenda
        val legend: Legend = pieChart_graph.getLegend()
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setTextSize(16F)
        //quando clicco su un elemento lo evidenzio
        pieChart_graph.isHighlightPerTapEnabled = true
        //animazione iniziale
        pieChart_graph.animateY(1000, Easing.EaseInOutQuart)
        //colore del buco
        pieChart_graph.setHoleColor(Color.parseColor(resources.getString(R.color.white.toInt())))
    }

    private fun popolaGrafico(){
        val pieEntries: ArrayList<PieEntry> = ArrayList()
        val label = ""
        //initializing data
        val typeAmountMap: MutableMap<String, Double> = HashMap()
        typeAmountMap[getString(R.string.credits)] = totCrediti
        typeAmountMap[getString(R.string.debts)] = totDebiti
        //initializing colors for the entries
        val colors: ArrayList<Int> = ArrayList()
        colors.add(Color.parseColor(resources.getString(R.color.green.toInt())))
        colors.add(Color.parseColor(resources.getString(R.color.orange.toInt())))
        //input data and fit data into pie chart entry
        for (type in typeAmountMap.keys) {
            pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
        }

        //collecting the entries with label name
        val pieDataSet = PieDataSet(pieEntries, label)
        //setting text size of the value
        pieDataSet.valueTextSize = 12f
        //providing color list for coloring different entries
        pieDataSet.colors = colors
        //grouping the data set from entry to chart
        val pieData = PieData(pieDataSet)
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true)
        //Set percentage mode
        pieData.setValueFormatter(PercentFormatter())

        pieChart_graph.data = pieData
        pieChart_graph.invalidate()
    }

    /*Gestione click card */
    val cardOnClickListener= View.OnClickListener { view ->
        val int = Intent(mContext, StoricoActivity::class.java)
        startActivity(int)
    }

}