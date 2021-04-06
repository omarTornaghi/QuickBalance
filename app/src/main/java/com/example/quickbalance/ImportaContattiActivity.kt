package com.example.quickbalance

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_importa_contatti.*


class ImportaContattiActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_importa_contatti)
        topAppBar.setNavigationOnClickListener(navigationIconOnClickListener)
        topAppBar.setOnMenuItemClickListener(this)
        disabilitaIconaTopAppBar()
    }

    private fun disabilitaIconaTopAppBar(){
        val moveLocationMenuItem = topAppBar.menu.findItem(R.id.checkIcon)
        moveLocationMenuItem.icon.mutate().alpha = 130
        moveLocationMenuItem.isEnabled = false
    }

    private fun abilitaIconaTopAppBar(){
        val moveLocationMenuItem = topAppBar.menu.findItem(R.id.checkIcon)
        moveLocationMenuItem.icon.mutate().alpha = 255
        moveLocationMenuItem.isEnabled = true
    }

    override fun onMenuItemClick(item:MenuItem):Boolean {
        when (item.itemId){
            R.id.checkIcon-> {
                //Importo i contatti nell'activity di creazione
                return true
            }
        }
        return false;
    }

    val navigationIconOnClickListener= View.OnClickListener {
        finish()
    }
}