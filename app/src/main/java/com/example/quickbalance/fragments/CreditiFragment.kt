package com.example.quickbalance.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quickbalance.Adapters.CreditAdapter
import com.example.quickbalance.DataTypes.CreditType
import com.example.quickbalance.NuovaOpActivity
import com.example.quickbalance.R
import kotlinx.android.synthetic.main.fragment_crediti.*
import kotlin.collections.ArrayList

class CreditiFragment() : Fragment(){
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
        editTextInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Call onDone result here
                    Log.d("XXXX", "CERCO")
                true
            }
            false
        }
        //Set dello spinner
        setSpinner()
        //set dello stato
        editTextInput.setText(stringTextInput)
        spinnerView.setSelection(intCampo)
        //Set del recyclerView
        val recyclerViewAdapter = setRecyclerView()
        //Set del click del floating button
        floatinButtonAggiungi.setOnClickListener(addFBOnClickListener)
        /* Popolazione */
        data = ArrayList<CreditType>()
        data.add(CreditType("Tor", "Prova", 300.00, 150.00, "3387135186", "26/11/2000", null))
        data.add(CreditType("Torn", "Prova", 300.00, 150.00, "3387135186", "26/11/2000", null))
        data.add(CreditType("Torna", "Prova", 300.00, 150.00, "3387135186", "26/11/2000", null))
        data.add(CreditType("Tornag", "Prova", 300.00, 150.00, "3387135186", "26/11/2000", null))
        data.add(CreditType("Tornaghi", "Prova", 300.00, 150.00, "3387135186", "26/11/2000", null))
        data.add(CreditType("Tornaghi O", "Prova", 300.00, 150.00, "3387135186", "26/11/2000", null))
        data.add(CreditType("Tornaghi Om", "Prova", 300.00, 150.00, "3387135186", "26/11/2000", null))
        data.add(CreditType("Tornaghi Oma", "Prova", 300.00, 150.00, "3387135186", "26/11/2000", null))
        data.add(CreditType("Tornaghi Omar", "Prova", 300.00, 150.00, "3387135186", "26/11/2000", null))
        data.add(CreditType("Tornaghi Omar2", "Prova", 300.00, 150.00, "3387135186", "26/11/2000", null))

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

    val addFBOnClickListener= View.OnClickListener {
        val int:Intent = Intent(mContext, NuovaOpActivity::class.java)
        startActivity(int)
    }

}