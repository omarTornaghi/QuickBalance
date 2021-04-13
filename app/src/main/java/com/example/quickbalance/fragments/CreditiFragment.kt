package com.example.quickbalance.fragments

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quickbalance.Adapters.CreditAdapter
import com.example.quickbalance.DataTypes.CreditType
import com.example.quickbalance.NuovaOpActivity
import com.example.quickbalance.R
import kotlinx.android.synthetic.main.fragment_crediti.*


class CreditiFragment : Fragment(){
    private lateinit var mContext: Context
    private lateinit var data: ArrayList<CreditType>
    private var stringTextInput: String = ""
    private var intCampo: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        if (savedInstanceState != null) {
            stringTextInput = savedInstanceState.getString("testoTextViewRicerca")!!
            intCampo = savedInstanceState.getInt("testoSpinner")!!
        }
        /* Popolazione */
        data = ArrayList<CreditType>()
        data.add(CreditType("Tor", "Prova", 300.00, 150.00, "3387135186", "26/11/2000", null))
        data.add(CreditType("Torn", "Prova", 300.00, 150.00, "3387135186", "26/11/2000", null))
        data.add(CreditType("Torna", "Prova", 300.00, 150.00, "3387135186", "26/11/2000", null))
        data.add(CreditType("Tornag", "Prova", 300.00, 150.00, "3387135186", "26/11/2000", null))
        data.add(CreditType("Tornaghi", "Prova", 300.00, 150.00, "3387135186", "26/11/2000", null))
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //set dello stato
        editTextInput.setText(stringTextInput)
        spinnerView.setSelection(intCampo)
        editTextInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                editTextInput.clearFocus()
                val imm = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0)
                Log.d("XXXX", "CERCO")
            }
            true
        }
        //Set del click del floating button
        floatinButtonAggiungi.setOnClickListener(addFBOnClickListener)
        //Set dello spinner
        setSpinner()
        Handler(Looper.getMainLooper()).postDelayed({
            //Set del recyclerView
            val recyclerViewAdapter = setRecyclerView()
            // Popolazione
            recyclerViewAdapter.updateTasks(data)
        }, 0)

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