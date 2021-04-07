package com.example.quickbalance

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quickbalance.Adapters.ContattoAdapter
import com.example.quickbalance.DataTypes.ContattoType
import com.example.quickbalance.DataTypes.PartecipanteType
import kotlinx.android.synthetic.main.activity_importa_contatti.*
import kotlinx.android.synthetic.main.activity_importa_contatti.recyclerView
import kotlinx.android.synthetic.main.activity_importa_contatti.textViewDatiVuoti
import kotlinx.android.synthetic.main.activity_importa_contatti.topAppBar

class ImportaContattiActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener {
    private lateinit var recyclerViewAdapter: ContattoAdapter
    private var listContattiSelezionati: ArrayList<PartecipanteType> = ArrayList()
    private var listContatti: ArrayList<ContattoType> = ArrayList()
    var namePhoneMap: HashMap<String, String> = HashMap()

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
                ricerca()
                true
            }
            false
        }
        editTextRicercaContatto.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                ricerca()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        //RecyclerView
        recyclerViewAdapter = setRecyclerView()
        if (savedInstanceState != null) {
            editTextRicercaContatto.setText(savedInstanceState.getString("editTextRicerca"))
            listContatti = savedInstanceState.getParcelableArrayList<ContattoType>("listContatti") as ArrayList<ContattoType>
            listContattiSelezionati = savedInstanceState.getParcelableArrayList<PartecipanteType>("listContattiSelezionati") as ArrayList<PartecipanteType>
            recyclerViewAdapter.updateTasks(savedInstanceState.getParcelableArrayList<ContattoType>("listContattiRecycler") as ArrayList<ContattoType>)
        }else
        {
            richiediPermessoContatti()
            listContatti = ArrayList(recyclerViewAdapter.getList().map { it.copy() })
            circularProgress.visibility = View.GONE
        }
    }

    fun ricerca(){
        if (editTextRicercaContatto.text.isNotBlank()) {
            var listaRicerca: ArrayList<ContattoType> =
                ArrayList(listContatti.filter { it ->
                    it.generalita!!.toLowerCase().contains(editTextRicercaContatto.text.toString().toLowerCase())
                })
            recyclerViewAdapter.updateTasks(listaRicerca)
        }
        else recyclerViewAdapter.updateTasks(listContatti)
    }

    fun richiediPermessoContatti() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_CONTACTS
                    )
                ) {
                    val builder: androidx.appcompat.app.AlertDialog.Builder =
                        androidx.appcompat.app.AlertDialog.Builder(this)
                    builder.setTitle(getString(R.string.read_contacts_permission))
                    builder.setPositiveButton(android.R.string.ok, null)
                    builder.setMessage(getString(R.string.read_contacts_permission_message))
                    builder.setOnDismissListener(DialogInterface.OnDismissListener {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.READ_CONTACTS
                            ), PERMISSIONS_REQUEST_READ_CONTACTS
                        )
                    })
                    builder.show()
                } else {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.READ_CONTACTS
                        ), PERMISSIONS_REQUEST_READ_CONTACTS
                    )
                }
            } else {
                getContactList()
            }
        } else {
            getContactList()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    getContactList()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.disabled_read_contacts_permission),
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
        }
    }

    private fun getContactList() {
        val phones: Cursor? = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        if (phones != null) {
            while (phones.moveToNext()) {
                val name: String =
                    phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                var phoneNumber: String =
                    phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                phoneNumber = phoneNumber.replace("[()\\s-]+".toRegex(), "")
                namePhoneMap.put(phoneNumber, name)
            }
        }
        var contactList: ArrayList<ContattoType> = ArrayList()
        for ((key, value) in namePhoneMap) {
            contactList.add(ContattoType(value, key, false))
        }
        recyclerViewAdapter.updateTasks(ArrayList(contactList.sortedWith(compareBy({ it.generalita }))))
        if (phones != null) {
            phones.close()
        }
    }

    private fun setRecyclerView(): ContattoAdapter {
        /* Set del recycler view */
        val recyclerViewAdapter = ContattoAdapter(arrayListOf(), textViewDatiVuoti) {
            val rIt: ContattoType = it
            var idUno:Int= listContatti.indexOf(it)
            rIt.selezionato = !rIt.selezionato!!
            recyclerViewAdapter.setItem(it, rIt)
            listContatti.set(idUno, rIt)

            var idDue:Int = listContatti.indexOf(rIt)

            val partecipante = PartecipanteType(rIt.generalita, rIt.numeroTelefono)
            if (rIt.selezionato == true)
                listContattiSelezionati.add(partecipante)
            else
                listContattiSelezionati.remove(partecipante)

            if (listContattiSelezionati.size > 0) abilitaIconaTopAppBar() else disabilitaIconaTopAppBar()
        }
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        return recyclerViewAdapter
    }

    private fun disabilitaIconaTopAppBar() {
        val moveLocationMenuItem = topAppBar.menu.findItem(R.id.checkIcon)
        moveLocationMenuItem.icon.mutate().alpha = 130
        moveLocationMenuItem.isEnabled = false
    }

    private fun abilitaIconaTopAppBar() {
        val moveLocationMenuItem = topAppBar.menu.findItem(R.id.checkIcon)
        moveLocationMenuItem.icon.mutate().alpha = 255
        moveLocationMenuItem.isEnabled = true
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.checkIcon -> {
                //Importo i contatti nell'activity di creazione
                val intent = Intent().apply {
                    putParcelableArrayListExtra("listaContatti", listContattiSelezionati)
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
                return true
            }
        }
        return false;
    }

    val navigationIconOnClickListener = View.OnClickListener {
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("editTextRicerca", editTextRicercaContatto.text.toString())
        outState.putParcelableArrayList("listContatti", listContatti)
        outState.putParcelableArrayList("listContattiSelezionati", listContattiSelezionati)
        outState.putParcelableArrayList("listContattiRecycler", recyclerViewAdapter.getList())
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    companion object {
        val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }

}