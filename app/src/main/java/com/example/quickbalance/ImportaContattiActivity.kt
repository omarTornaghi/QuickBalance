package com.example.quickbalance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_creazione_stepuno.*

class ImportaContattiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_importa_contatti)
        topAppBar.setNavigationOnClickListener(navigationIconOnClickListener)
    }


    val navigationIconOnClickListener= View.OnClickListener {
        finish()
    }
}