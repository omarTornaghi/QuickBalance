package com.example.quickbalance.DataTypes

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.example.quickbalance.R


data class NotificaType(var id:Int, var numGiorni:Int, var idTransazione:Int , val context:Context?){
    constructor(): this(0, 0,0,null)
    override fun toString(): String {
        when(numGiorni){
            0-> if(context != null){
                return context.getString(R.string.day)
            }
            1-> if (context != null) {
                return "$numGiorni ${context.getString(R.string.day_before)}"
            }
            else-> if (context != null) {
                return "$numGiorni ${context.getString(R.string.days_before)}"
            }
        }
        return ""
    }

    companion object{
        val TABLE_NAME = "Notifica"
        val COL_ID = "id"
        val COL_NUM_GIORNI_PRIMA = "numGiorniPrima"
        val COL_TRANSAZIONE_ID = "transazioneId"

        val QUERY_CREAZIONE = "CREATE TABLE $TABLE_NAME(\n" +
                "\t$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t$COL_NUM_GIORNI_PRIMA INTEGER,\n" +
                "\t$COL_TRANSAZIONE_ID INTEGER REFERENCES ${TransazioneType.TABLE_NAME}(${TransazioneType.COL_ID}) ON DELETE CASCADE\n" +
                ");"
        val QUERY_CANCELLAZIONE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}