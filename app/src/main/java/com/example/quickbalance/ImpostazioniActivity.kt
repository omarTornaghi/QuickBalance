package com.example.quickbalance

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.example.quickbalance.Utils.ValutaUtils
import kotlinx.android.synthetic.main.activity_impostazioni.*
import java.util.*


class ImpostazioniActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_impostazioni)
        topAppBar.setNavigationOnClickListener{finish()}

        textViewLinguaSelezionata.setText(Locale.getDefault().displayLanguage)
        cardCambiaValuta.setOnClickListener {
            val int = Intent(this, CambiaValutaActivity::class.java)
            startActivity(int)
        }
        cardCambiaLingua.setOnClickListener{
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            //restartApp()
        }
    }

    override fun onResume() {
        super.onResume()
        textViewValutaSelezionata.text = ValutaUtils.getSelectedCurrencySymbol(this)
    }

    private fun restartApp() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        val mPendingIntentId: Int = 1
        val mPendingIntent = PendingIntent.getActivity(
            applicationContext,
            mPendingIntentId,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val mgr = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mgr[AlarmManager.RTC, System.currentTimeMillis() + 100] = mPendingIntent
        System.exit(0)
    }



}