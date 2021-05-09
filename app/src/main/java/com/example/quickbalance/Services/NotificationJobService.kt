package com.example.quickbalance.Services

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.quickbalance.DataTypes.TransazioneType
import com.example.quickbalance.Database.DbHelper
import com.example.quickbalance.R
import com.example.quickbalance.Utils.ValutaUtils
import java.text.SimpleDateFormat
import java.util.*


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class NotificationJobService : JobService() {
    override fun onStartJob(params: JobParameters): Boolean {
        val dbHelper = DbHelper(applicationContext)
        val listaTransazioni = dbHelper.getTransazioniInScadenza()
        listaTransazioni.forEach {
            createNotification(it)
        }
        return true
    }

    private fun createNotification(transazione:TransazioneType) {

        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, default_notification_channel_id)
        var titoloNotifica:String = ""
        var tipo:String=""
        if(transazione.credito)
            tipo = applicationContext.getString(R.string.credit)
        else
            tipo = applicationContext.getString(R.string.debit)
        titoloNotifica += tipo + " " + applicationContext.getString(R.string.will_expire)
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val oggi = sdf.format(Date())
        if(transazione.dataFine == oggi)
            titoloNotifica+= " " + applicationContext.getString(R.string.today)
        else
            titoloNotifica+= " " + transazione.dataFine

        val contentNotifica:String = tipo + " " + applicationContext.getString(R.string.with) + " " + transazione.generalita + "\n" + applicationContext.getString(R.string.total_import_field) + " " + transazione.soldiTotali + ValutaUtils.getSelectedCurrencySymbol(applicationContext)
        mBuilder.setContentTitle(titoloNotifica)
        mBuilder.setContentText(contentNotifica)
        mBuilder.setTicker(applicationContext.getString(R.string.app_name))
        mBuilder.setSmallIcon(R.mipmap.ic_logo)
        mBuilder.setAutoCancel(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "NOTIFICATION_CHANNEL_NAME",
                importance
            )
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
            if (false) {
                error("Assertion failed")
            }
            mNotificationManager.createNotificationChannel(notificationChannel)
        }
        if (false) {
            error("Assertion failed")
        }
        mNotificationManager.notify(System.currentTimeMillis().toInt(), mBuilder.build())
    }

    override fun onStopJob(jobParameters: JobParameters): Boolean {
        return true
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "10001"
        private const val default_notification_channel_id = "default"
    }
}