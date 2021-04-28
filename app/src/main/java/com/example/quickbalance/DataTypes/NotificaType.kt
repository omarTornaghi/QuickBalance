package com.example.quickbalance.DataTypes

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.example.quickbalance.R


data class NotificaType(var numGiorni:Int, val context:Context){
    override fun toString(): String {
        when(numGiorni){
            1->return "$numGiorni ${context.getString(R.string.day_before)}"
            else-> return "$numGiorni ${context.getString(R.string.days_before)}"
        }
    }

    companion object{
        val QUERY_CREAZIONE = "CREATE TABLE Notifica(\n" +
                "\tId INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\tnumGiorniPrima INTEGER,\n" +
                "\ttransazioneId INTEGER REFERENCES Transazione(Id) ON DELETE CASCADE\n" +
                ");"
        val QUERY_CANCELLAZIONE_TABLE = "DROP TABLE IF EXISTS Notifica"
    }
}