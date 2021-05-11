package com.example.quickbalance.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quickbalance.Adapters.TransazioneAdapter
import com.example.quickbalance.DataTypes.TransazioneType
import com.example.quickbalance.Database.DbHelper
import com.example.quickbalance.Activities.NuovaOpActivity
import com.example.quickbalance.R
import com.example.quickbalance.Utils.InitializeAsync
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_crediti.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class CreditiFragment : Fragment() {
    private lateinit var dbHelper: DbHelper
    private lateinit var mContext: Context
    private lateinit var data: ArrayList<TransazioneType>
    private lateinit var recyclerViewAdapter: TransazioneAdapter
    private lateinit var cdSpinnerAdapter: ArrayAdapter<CharSequence>
    private var stringTextInput: String = ""
    private var intCampo: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        if (savedInstanceState != null) {
            stringTextInput = savedInstanceState.getString("testoTextViewRicerca")!!
            intCampo = savedInstanceState.getInt("testoSpinner")
            data = savedInstanceState.getParcelableArrayList<TransazioneType>("data") as ArrayList<TransazioneType>
        }
        InitializeAsync {
            //Set spinner
            cdSpinnerAdapter = ArrayAdapter.createFromResource(
                mContext,
                R.array.spinnerCD,
                R.layout.custom_spinnercd_layout
            )
            cdSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinnercd_dropdown_layout);
            //Set recyclerView
            recyclerViewAdapter = TransazioneAdapter(mutableListOf(),
                object : TransazioneAdapter.ListAdapterListener {
                    override fun onClickAtRemoveButton(position: Int) {
                        val dialogClickListener =
                            DialogInterface.OnClickListener { dialog, which ->
                                when (which) {
                                    DialogInterface.BUTTON_POSITIVE -> {
                                        dbHelper.deleteTransazione(recyclerViewAdapter.getItem(position))
                                        data.remove(recyclerViewAdapter.getItem(position))
                                        recyclerViewAdapter.removeItem(position)
                                        if(data.size == 0){
                                            imageViewEmpty.visibility = View.VISIBLE
                                            textViewEmpty.visibility = View.VISIBLE
                                        }
                                    }
                                }
                            }
                        val ab: AlertDialog.Builder = AlertDialog.Builder(mContext)
                        ab.setMessage(mContext.getString(R.string.confirm_delete_credit)).setPositiveButton(
                            mContext.getString(
                                R.string.yes
                            ), dialogClickListener
                        )
                            .setNegativeButton(mContext.getString(R.string.no), dialogClickListener).show()
                    }
            }, mContext)

        }
    }

    override fun onResume() {
        super.onResume()
        dbHelper = DbHelper(mContext)
        data = dbHelper.getCreditiAttivi() as ArrayList<TransazioneType>
        recyclerViewAdapter.updateTasks(data)
        if(data.size > 0) {
            imageViewEmpty.visibility = View.GONE
            textViewEmpty.visibility = View.GONE
        }else {
            imageViewEmpty.visibility = View.VISIBLE
            textViewEmpty.visibility = View.VISIBLE
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_crediti, container, false)
        InitializeAsync {
            view.post {
                var recycler_view = view.findViewById<RecyclerView>(R.id.recycler_view)
                recycler_view.adapter = recyclerViewAdapter
                recycler_view.layoutManager = LinearLayoutManager(mContext)
                var spinnerView = view.findViewById<Spinner>(R.id.spinnerView)
                spinnerView.setAdapter(cdSpinnerAdapter)
                spinnerView.setSelection(intCampo)
                var first = true
                spinnerView.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if(!first){
                            ricerca()
                        }
                        first = false
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }
                }
            }
        }
        var editTextInput = view.findViewById<EditText>(R.id.editTextInput)
        editTextInput.setText(stringTextInput)
        editTextInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                ricerca()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(editTextInput.text.toString().isNotBlank())
                    editTextInput.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_icon,0,R.drawable.ic_baseline_clear_24, 0)
                else
                    editTextInput.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_icon,0,0, 0)
            }
        })
        editTextInput.setOnTouchListener(object : View.OnTouchListener {
            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val DRAWABLE_RIGHT = 2;
                if (event != null) {
                    try {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            if (event.getRawX() >= (editTextInput.getRight() - editTextInput.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds()
                                    .width())
                            ) {
                                editTextInput.text.clear()
                                editTextInput.requestFocus()
                                return true;
                            }
                        }
                    }
                    catch(ex:Exception){}
                }
                return false;
            }
        })
        editTextInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                editTextInput.clearFocus()
                val imm = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0)
                ricerca()

            }
            true
        }
        var floatingButtonAggiungi =
            view.findViewById<FloatingActionButton>(R.id.floatinButtonAggiungi)
        //Set del click del floating button
        floatingButtonAggiungi.setOnClickListener(addFBOnClickListener)

        return view
    }

    fun ricerca(){
        if(editTextInput.text.isNotBlank()){
            var listaRicerca:ArrayList<TransazioneType> = ArrayList()
            when(spinnerView.selectedItemPosition){
                0->{
                    listaRicerca =
                        ArrayList(data.filter {
                            it.generalita!!.toLowerCase(Locale.ROOT)
                                .contains(editTextInput.text.toString().toLowerCase(Locale.ROOT))
                        })

                }
                1->{
                    listaRicerca =
                        ArrayList(data.filter { it.descrizione!!.toLowerCase(Locale.ROOT).contains(editTextInput.text.toString().toLowerCase(Locale.ROOT)) })
                }
                2->{
                    listaRicerca =
                        ArrayList(data.filter { it.numeroTelefono!!.toLowerCase(Locale.ROOT).contains(editTextInput.text.toString().toLowerCase(Locale.ROOT)) })
                }
            }
            recyclerViewAdapter.updateTasks(listaRicerca)
        }
        else
        {
            recyclerViewAdapter.updateTasks(data)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("testoTextViewRicerca", editTextInput.text.toString())
        outState.putInt("testoSpinner", spinnerView.selectedItemPosition)
        outState.putParcelableArrayList("data", data)
    }

    val addFBOnClickListener = View.OnClickListener {
        val int: Intent = Intent(mContext, NuovaOpActivity::class.java)
        int.putExtra("operazioneCredito", true)
        int.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
        startActivity(int)
    }

}