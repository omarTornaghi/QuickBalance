package com.example.quickbalance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class ImportaContattiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_importa_contatti)
    }


    val navigationIconOnClickListener= View.OnClickListener {
        finish()
    }
}