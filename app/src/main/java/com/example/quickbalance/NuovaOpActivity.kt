package com.example.quickbalance

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_creazione_stepuno.*

class NuovaOpActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creazione_stepuno)

        topAppBar.setNavigationOnClickListener(navigationIconOnClickListener)
    }
    val navigationIconOnClickListener= View.OnClickListener {
        finish()
    }
}