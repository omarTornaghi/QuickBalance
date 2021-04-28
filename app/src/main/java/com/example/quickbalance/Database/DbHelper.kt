package com.example.quickbalance.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.example.quickbalance.DataTypes.NotificaType
import com.example.quickbalance.DataTypes.TransazioneType
import com.example.quickbalance.Utils.FieldUtils.Companion.fromDateDBTONormal
import com.example.quickbalance.Utils.FieldUtils.Companion.fromDateNormalTODb

class DbHelper(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TransazioneType.QUERY_CREAZIONE_TABLE)
        db.execSQL(NotificaType.QUERY_CREAZIONE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if(oldVersion < newVersion) {
            db.execSQL(NotificaType.QUERY_CANCELLAZIONE_TABLE)
            db.execSQL(TransazioneType.QUERY_CANCELLAZIONE_TABLE)
            onCreate(db)
        }


    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun insertTransazione(transazione:TransazioneType){
        val database = this.writableDatabase
        var contentValues = ContentValues()
        contentValues.put(TransazioneType.COL_GENERALITA, transazione.generalita)
        contentValues.put(TransazioneType.COL_DESCRIZIONE, transazione.descrizione)
        contentValues.put(TransazioneType.COL_TELEFONO, transazione.numeroTelefono)
        contentValues.put(TransazioneType.COL_DATAINIZIO, fromDateNormalTODb(transazione.dataInizio))
        contentValues.put(TransazioneType.COL_DATASCADENZA, fromDateNormalTODb(transazione.dataFine))
        contentValues.put(TransazioneType.COL_IMPORTO_TOTALE, transazione.soldiTotali)
        contentValues.put(TransazioneType.COL_IMPORTO_DATO, transazione.soldiRicevuti)
        contentValues.put(TransazioneType.COL_TIPO, if(transazione.credito)1 else 0)
        val result = database.insert(TransazioneType.TABLE_NAME, null, contentValues)
        val successo = result == (0).toLong()
    }

    fun getTransazioniAttive(tipo:Int):MutableList<TransazioneType>{
        val list: MutableList<TransazioneType> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM ${TransazioneType.TABLE_NAME} WHERE ${TransazioneType.COL_TIPO} = tipo AND ${TransazioneType.COL_IMPORTO_DATO} < ${TransazioneType.COL_IMPORTO_TOTALE}"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst())
        {
            do {
                val transazione = TransazioneType()
                transazione.id = result.getInt(result.getColumnIndex(TransazioneType.COL_ID))
                transazione.generalita = result.getString(result.getColumnIndex(TransazioneType.COL_GENERALITA))
                transazione.descrizione = result.getString(result.getColumnIndex(TransazioneType.COL_DESCRIZIONE))
                transazione.numeroTelefono = result.getString(result.getColumnIndex(TransazioneType.COL_TELEFONO))
                transazione.dataInizio = fromDateDBTONormal(result.getString(result.getColumnIndex(TransazioneType.COL_DATAINIZIO)))
                transazione.dataFine = fromDateDBTONormal(result.getString(result.getColumnIndex(TransazioneType.COL_DATASCADENZA)))
                transazione.soldiTotali = result.getDouble(result.getColumnIndex(TransazioneType.COL_IMPORTO_TOTALE))
                transazione.soldiRicevuti = result.getDouble(result.getColumnIndex(TransazioneType.COL_IMPORTO_DATO))
                transazione.credito = if(tipo == 1)true else false
                list.add(transazione)
            }
            while (result.moveToNext())
        }
        result.close()
        return list
    }

    fun getCreditiAttivi():MutableList<TransazioneType>{
        return getTransazioniAttive(1)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "QB_data.db"
    }


}