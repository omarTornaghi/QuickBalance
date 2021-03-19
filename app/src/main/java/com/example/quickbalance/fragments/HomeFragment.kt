package com.example.quickbalance.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.quickbalance.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun initPieChart() {
        //using percentage as values instead of amount
        pieChart_graph.setUsePercentValues(true)

        //remove the description label on the lower left corner, default true if not set
        pieChart_graph.description.isEnabled = false

        //enabling the user to rotate the chart, default true
        pieChart_graph.isRotationEnabled = true
        //adding friction when rotating the pie chart
        pieChart_graph.dragDecelerationFrictionCoef = 0.9f
        //setting the first entry start from right hand side, default starting from top
        pieChart_graph.rotationAngle = 0f
        //Set della legenda
        val legend: Legend = pieChart_graph.getLegend()
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        //legend.setFormSize(20F)
        legend.setTextSize(16F)
        //highlight the entry when it is tapped, default true if not set
        pieChart_graph.isHighlightPerTapEnabled = true
        //adding animation so the entries pop up from 0 degree
        pieChart_graph.animateY(1400, Easing.EaseInOutQuad)
        //setting the color of the hole in the middle, default white
        pieChart_graph.setHoleColor(Color.parseColor(resources.getString(R.color.white.toInt())))
    }

    private fun showPieChart(){
        val pieEntries: ArrayList<PieEntry> = ArrayList()
        val label = ""

        //initializing data
        val typeAmountMap: MutableMap<String, Int> = HashMap()
        typeAmountMap[getString(R.string.total_credit)] = 200
        typeAmountMap[getString(R.string.total_debts)] = 230
        //initializing colors for the entries
        val colors: ArrayList<Int> = ArrayList()
        colors.add(Color.parseColor(resources.getString(R.color.green.toInt())))
        colors.add(Color.parseColor(resources.getString(R.color.orange.toInt())))
        //input data and fit data into pie chart entry

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPieChart()
        showPieChart()
    }


}