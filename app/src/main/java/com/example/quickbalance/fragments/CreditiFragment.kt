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
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quickbalance.Adapters.CreditAdapter
import com.example.quickbalance.DataTypes.CreditType
import com.example.quickbalance.NuovaOpActivity
import com.example.quickbalance.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_crediti.*


class CreditiFragment : Fragment(){
    private lateinit var mContext: Context
    private lateinit var data: ArrayList<CreditType>
    private lateinit var recyclerViewAdapter:CreditAdapter
    private lateinit var cdSpinnerAdapter:ArrayAdapter<CharSequence>
    private var stringTextInput: String = ""
    private var intCampo: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        if (savedInstanceState != null) {
            stringTextInput = savedInstanceState.getString("testoTextViewRicerca")!!
            intCampo = savedInstanceState.getInt("testoSpinner")!!
        }
        Thread {
            try {
                //Set spinner
                cdSpinnerAdapter= ArrayAdapter.createFromResource(
                    mContext,
                    R.array.spinnerCD,
                    R.layout.custom_spinnercd_layout
                )
                cdSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinnercd_dropdown_layout);
                //Set recyclerView
                recyclerViewAdapter = CreditAdapter(mutableListOf())
                // Popolazione
                data = ArrayList<CreditType>()
                data.add(CreditType("Tor", "Prova", 300.00, 150.00, "3387135186", "26/11/2000", null))
                data.add(CreditType("Torn", "Prova", 300.00, 150.00, "3387135186", "26/11/2000", null))
                data.add(CreditType("Torna", "Prova", 300.00, 150.00, "3387135186", "26/11/2000", null))
                data.add(
                    CreditType(
                        "Tornag",
                        "Prova",
                        300.00,
                        150.00,
                        "3387135186",
                        "26/11/2000",
                        null
                    )
                )
                data.add(
                    CreditType(
                        "Tornaghi",
                        "Prova",
                        300.00,
                        150.00,
                        "3387135186",
                        "26/11/2000",
                        null
                    )
                )
                recyclerViewAdapter.updateTasks(data)
            } catch (ignored: Exception) {
            }
        }.start()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_crediti, container, false)
        var editTextInput = view.findViewById<EditText>(R.id.editTextInput)
        editTextInput.setText(stringTextInput)
        var spinnerView = view.findViewById<Spinner>(R.id.spinnerView)
        spinnerView.setAdapter(cdSpinnerAdapter)
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
        var floatingButtonAggiungi = view.findViewById<FloatingActionButton>(R.id.floatinButtonAggiungi)
        //Set del click del floating button
        floatingButtonAggiungi.setOnClickListener(addFBOnClickListener)
        var recycler_view = view.findViewById<RecyclerView>(R.id.recycler_view)
        recycler_view.adapter = recyclerViewAdapter
        recycler_view.layoutManager = LinearLayoutManager(mContext)
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("testoTextViewRicerca", editTextInput.text.toString())
        outState.putInt("testoSpinner", spinnerView.selectedItemPosition)
    }

    val addFBOnClickListener= View.OnClickListener {
        val int:Intent = Intent(mContext, NuovaOpActivity::class.java)
        int.putExtra("operazioneCredito", true)
        startActivity(int)
    }

}