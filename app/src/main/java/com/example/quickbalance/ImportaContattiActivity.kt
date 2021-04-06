package com.example.quickbalance

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quickbalance.Adapters.ContattoAdapter
import com.example.quickbalance.DataTypes.ContattoType
import kotlinx.android.synthetic.main.activity_importa_contatti.*
import kotlinx.android.synthetic.main.activity_importa_contatti.topAppBar

class ImportaContattiActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener {
    private lateinit var recyclerViewAdapter:ContattoAdapter
    private var contattiSelezionati:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_importa_contatti)
        disabilitaIconaTopAppBar()
        topAppBar.setNavigationOnClickListener(navigationIconOnClickListener)
        topAppBar.setOnMenuItemClickListener(this)
        editTextRicercaContatto.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                editTextRicercaContatto.clearFocus()
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(
                    editTextRicercaContatto.getWindowToken(),
                    InputMethodManager.RESULT_UNCHANGED_SHOWN
                )
                Log.d("XXXX", "CERCO")
                true
            }
            false
        }
        //RecyclerView
        recyclerViewAdapter = setRecyclerView()

        recyclerViewAdapter.addItem(ContattoType("Tornaghi Omar", "3387135186", false))
        recyclerViewAdapter.addItem(ContattoType("Tornaghi Luca", "3387135186", false))
        recyclerViewAdapter.addItem(ContattoType("Tornaghi Alessio", "3387135186", false))
        recyclerViewAdapter.addItem(ContattoType("Tornaghi Simona", "3387135186", false))
        recyclerViewAdapter.addItem(ContattoType("Tornaghi Mara", "3387135186", false))
        recyclerViewAdapter.addItem(ContattoType("Tornaghi Massimo", "3387135186", false))
        recyclerViewAdapter.addItem(ContattoType("Tornaghi Gabriele", "3387135186", false))
        recyclerViewAdapter.addItem(ContattoType("Tornaghi Gino", "3387135186", false))
        recyclerViewAdapter.addItem(ContattoType("Tornaghi Gianmarco", "3387135186", false))
        recyclerViewAdapter.addItem(ContattoType("Tornaghi Lizia", "3387135186", false))
        recyclerViewAdapter.addItem(ContattoType("Tornaghi Giacomo", "3387135186", false))
        recyclerViewAdapter.addItem(ContattoType("Tornaghi Riccardo", "3387135186", false))
        recyclerViewAdapter.addItem(ContattoType("Tornaghi Stefano", "3387135186", false))

    }

    private fun setRecyclerView(): ContattoAdapter {
        /* Set del recycler view */
        val recyclerViewAdapter = ContattoAdapter(arrayListOf(), textViewDatiVuoti){
            val rIt:ContattoType = it
            rIt.selezionato = !rIt.selezionato!!
            recyclerViewAdapter.setItem(it, rIt)
            if(rIt.selezionato == true)
                contattiSelezionati++;
            else
                contattiSelezionati = if(contattiSelezionati == 0) 0 else contattiSelezionati - 1

            if(contattiSelezionati > 0) abilitaIconaTopAppBar() else disabilitaIconaTopAppBar()
        }
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        return recyclerViewAdapter
    }

    public fun disabilitaIconaTopAppBar(){
        val moveLocationMenuItem = topAppBar.menu.findItem(R.id.checkIcon)
        moveLocationMenuItem.icon.mutate().alpha = 130
        moveLocationMenuItem.isEnabled = false
    }

    public fun abilitaIconaTopAppBar(){
        val moveLocationMenuItem = topAppBar.menu.findItem(R.id.checkIcon)
        moveLocationMenuItem.icon.mutate().alpha = 255
        moveLocationMenuItem.isEnabled = true
    }

    override fun onMenuItemClick(item: MenuItem):Boolean {
        when (item.itemId){
            R.id.checkIcon -> {
                //Importo i contatti nell'activity di creazione
                return true
            }
        }
        return false;
    }

    val navigationIconOnClickListener= View.OnClickListener {
        finish()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}