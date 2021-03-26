package com.example.quickbalance.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quickbalance.Adapters.CreditAdapter
import com.example.quickbalance.DataTypes.CreditType
import com.example.quickbalance.R
import kotlinx.android.synthetic.main.fragment_crediti.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class CreditiFragment : Fragment() {
    private lateinit var mContext: Context
    private lateinit var crediti: ArrayList<CreditType>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
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
        return inflater.inflate(R.layout.fragment_crediti, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* Set dello spinner */
        var cdSpinnerAdapter:ArrayAdapter<CharSequence>  = ArrayAdapter.createFromResource(
            mContext,
            R.array.spinnerCD,
            R.layout.custom_spinnercd_layout
        );
        cdSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinnercd_dropdown_layout);
        spinnerView.setAdapter(cdSpinnerAdapter)
        /* Set del recycler view */
        //linearLayoutManager = LinearLayoutManager(mContext)
        //recycler_view.layoutManager = linearLayoutManager

        //adapter = CreditAdapter(crediti)
        //recycler_view.adapter = adapter

        val recyclerViewAdapter = CreditAdapter(mutableListOf())
        recycler_view.adapter = recyclerViewAdapter
        recycler_view.layoutManager = LinearLayoutManager(mContext)
        /* Popolazione */
        crediti = ArrayList<CreditType>()
        crediti.add(CreditType("Tornaghi Omar", "Calcetto", 500.00, 200.00, "3387135186","24/11/2000", null))

        recyclerViewAdapter.updateTasks(crediti)

    }
    fun String.getStringDate(initialFormat: String, requiredFormat: String, locale: Locale = Locale.getDefault()): String {
        return this.toDate(initialFormat, locale).toString(requiredFormat, locale)
    }

    fun String.toDate(format: String, locale: Locale = Locale.getDefault()): Date = SimpleDateFormat(format, locale).parse(this)

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }
}