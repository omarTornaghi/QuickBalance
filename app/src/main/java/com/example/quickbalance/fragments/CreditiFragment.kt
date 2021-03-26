package com.example.quickbalance.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.custom_spinnercd_layout.*
import kotlinx.android.synthetic.main.fragment_crediti.*
import kotlin.collections.ArrayList

class CreditiFragment() : Fragment() {
    private lateinit var mContext: Context
    private lateinit var data: ArrayList<CreditType>
    private var stringTextInput: String = ""
    private var intCampo: Int = 0

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("testoTextViewRicerca", editTextInput.text.toString())
        outState.putInt("testoSpinner", spinnerView.selectedItemPosition)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            stringTextInput = savedInstanceState.getString("testoTextViewRicerca")!!
            intCampo = savedInstanceState.getInt("testoSpinner")!!
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Set dello spinner
        setSpinner()
        //set dello stato
        editTextInput.setText(stringTextInput)
        spinnerView.setSelection(intCampo)

        //Set del recyclerView
        val recyclerViewAdapter = setRecyclerView()

        /* Popolazione */
        data = ArrayList<CreditType>()
        for (i in 1..10)
            data.add(
                CreditType(
                    "Tornaghi Omar",
                    "Prova",
                    100.00,
                    20.00,
                    "3387135186",
                    "23/02/2020",
                    "11/12/2020"
                )
            )
        recyclerViewAdapter.updateTasks(data)

    }

    private fun setRecyclerView(): CreditAdapter {
        /* Set del recycler view */
        val recyclerViewAdapter = CreditAdapter(mutableListOf())
        recycler_view.adapter = recyclerViewAdapter
        recycler_view.layoutManager = LinearLayoutManager(mContext)
        return recyclerViewAdapter
    }

    private fun setSpinner() {
        /* Set dello spinner */
        var cdSpinnerAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            mContext,
            R.array.spinnerCD,
            R.layout.custom_spinnercd_layout
        );
        cdSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinnercd_dropdown_layout);
        spinnerView.setAdapter(cdSpinnerAdapter)
    }



}