package com.example.quickbalance.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quickbalance.Adapters.CreditAdapter
import com.example.quickbalance.DataTypes.CreditType
import com.example.quickbalance.R
import kotlinx.android.synthetic.main.fragment_crediti.*
import kotlinx.android.synthetic.main.fragment_home.*

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
        linearLayoutManager = LinearLayoutManager(mContext)

        recycler_view.layoutManager = linearLayoutManager
        crediti = ArrayList<CreditType>()
        adapter = CreditAdapter(crediti)
        recycler_view.adapter = adapter

        /* Popolazione */
        val primoCredito:CreditType = CreditType("Titolo 1", "Sottotitolo 1")
        val secondoCredito:CreditType= CreditType("Titolo 2", "Sottotitolo 2")
        crediti.add(primoCredito)
        crediti.add(secondoCredito)
    }

}