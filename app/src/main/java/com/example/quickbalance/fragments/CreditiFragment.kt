package com.example.quickbalance.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quickbalance.Adapters.CreditAdapter
import com.example.quickbalance.DataTypes.CreditType
import com.example.quickbalance.R
import kotlinx.android.synthetic.main.fragment_crediti.*

class CreditiFragment : Fragment() {
    private lateinit var mContext: Context
    private lateinit var adapter: CreditAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var crediti: ArrayList<CreditType>
    /*
    private val lastVisibleItemPosition: Int
        get() = linearLayoutManager.findLastVisibleItemPosition()

    private fun setRecyclerViewScrollListener() {
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView.layoutManager!!.itemCount
                if (!imageRequester.isLoadingData && totalItemCount == lastVisibleItemPosition + 1) {
                    requestPhoto()
                }
            }
        })
    }*/

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
        var cdSpinnerAdapter:ArrayAdapter<CharSequence>  = ArrayAdapter.createFromResource(mContext, R.array.spinnerCD ,R.layout.custom_spinnercd_layout);
        cdSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinnercd_dropdown_layout);
        spinnerView.setAdapter(cdSpinnerAdapter)
        /* Set del recycler view */
        linearLayoutManager = LinearLayoutManager(mContext)
        recycler_view.layoutManager = linearLayoutManager
        crediti = ArrayList<CreditType>()
        adapter = CreditAdapter(crediti)
        recycler_view.adapter = adapter

        /* Popolazione */
        val primoCredito:CreditType = CreditType("Tornaghi Omar", "Calcetto", 12.57)
        val secondoCredito:CreditType= CreditType("Tornaghi Luca", "Compleanno mamma", 20.00)
        crediti.add(CreditType("Tornaghi Omar", "Calcetto", 12.57))
        crediti.add(CreditType("Tornaghi Luca", "Compleanno mamma", 20.00))
        crediti.add(CreditType("Tornaghi Alessio", "Soldi", 35.00))
        crediti.add(CreditType("Gallo Simona", "Casa", 120.00))
        crediti.add(CreditType("Gallo Mara", "Casa", 40.00))
        crediti.add(CreditType("Gallo Fabio", "Casa", 41.00))
        crediti.add(CreditType("Gallo Gino", "Casa", 42.00))
        crediti.add(CreditType("Masin Davide", "Casa", 43.00))
        crediti.add(CreditType("Pricolo Francesco", "Casa", 44.00))
    }

}