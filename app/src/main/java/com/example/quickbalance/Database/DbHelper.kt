package com.example.quickbalance.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.quickbalance.DataTypes.NotificaType
import com.example.quickbalance.DataTypes.TransazioneType
import com.example.quickbalance.Utils.FieldUtils.Companion.fromDateDBTONormal
import com.example.quickbalance.Utils.FieldUtils.Companion.fromDateNormalTODb
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DbHelper(var context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TransazioneType.QUERY_CREAZIONE_TABLE)
        db.execSQL(NotificaType.QUERY_CREAZIONE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) {
            db.execSQL(NotificaType.QUERY_CANCELLAZIONE_TABLE)
            db.execSQL(TransazioneType.QUERY_CANCELLAZIONE_TABLE)
            onCreate(db)
        }
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun insertTransazione(transazione: TransazioneType): Int {
        val database = this.writableDatabase
        val contentValues = getTransContentValues(transazione)
        database.insert(TransazioneType.TABLE_NAME, null, contentValues)
        val selectQuery = "SELECT * FROM " + TransazioneType.TABLE_NAME + " sqlite_sequence"
        val cursor = database.rawQuery(selectQuery, null)
        cursor.moveToLast()
        val id = cursor.getInt(cursor.getColumnIndex(TransazioneType.COL_ID))
        cursor.close()
        return id
    }

    private fun getTransContentValues(transazione: TransazioneType): ContentValues {
        val contentValues = ContentValues()
        contentValues.put(TransazioneType.COL_GENERALITA, transazione.generalita)
        contentValues.put(TransazioneType.COL_DESCRIZIONE, transazione.descrizione)
        contentValues.put(TransazioneType.COL_TELEFONO, transazione.numeroTelefono)
        contentValues.put(
            TransazioneType.COL_DATAINIZIO,
            fromDateNormalTODb(transazione.dataInizio)
        )
        contentValues.put(
            TransazioneType.COL_DATASCADENZA,
            fromDateNormalTODb(transazione.dataFine)
        )
        contentValues.put(
            TransazioneType.COL_DATAESTINZIONE,
            fromDateNormalTODb(transazione.dataEstinzione)
        )
        contentValues.put(TransazioneType.COL_IMPORTO_TOTALE, transazione.soldiTotali)
        contentValues.put(TransazioneType.COL_IMPORTO_DATO, transazione.soldiRicevuti)
        contentValues.put(
            TransazioneType.COL_TIPO,
            if (transazione.credito) TransazioneType.TIPO_CREDITO else TransazioneType.TIPO_DEBITO
        )
        return contentValues
    }

    fun getTransazioni(query: String): MutableList<TransazioneType> {
        val list: MutableList<TransazioneType> = ArrayList()
        val db = this.readableDatabase
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val transazione = TransazioneType()
                transazione.id = result.getInt(result.getColumnIndex(TransazioneType.COL_ID))
                transazione.generalita =
                    result.getString(result.getColumnIndex(TransazioneType.COL_GENERALITA))
                transazione.descrizione =
                    result.getString(result.getColumnIndex(TransazioneType.COL_DESCRIZIONE))
                transazione.numeroTelefono =
                    result.getString(result.getColumnIndex(TransazioneType.COL_TELEFONO))
                transazione.dataInizio = fromDateDBTONormal(
                    result.getString(
                        result.getColumnIndex(
                            TransazioneType.COL_DATAINIZIO
                        )
                    )
                )
                transazione.dataFine = fromDateDBTONormal(
                    result.getString(
                        result.getColumnIndex(
                            TransazioneType.COL_DATASCADENZA
                        )
                    )
                )
                transazione.dataEstinzione = fromDateDBTONormal(
                    result.getString(
                        result.getColumnIndex(
                            TransazioneType.COL_DATAESTINZIONE
                        )
                    )
                )
                transazione.soldiTotali =
                    result.getDouble(result.getColumnIndex(TransazioneType.COL_IMPORTO_TOTALE))
                transazione.soldiRicevuti =
                    result.getDouble(result.getColumnIndex(TransazioneType.COL_IMPORTO_DATO))
                transazione.credito =
                    if (result.getInt(result.getColumnIndex(TransazioneType.COL_TIPO)) == TransazioneType.TIPO_CREDITO) true else false
                list.add(transazione)
            } while (result.moveToNext())
        }
        result.close()
        return list
    }

    fun getTransazioniCompletate(numGiorni: Int): MutableList<TransazioneType> {
        if (numGiorni > 0) return getTransazioni("SELECT * FROM ${TransazioneType.TABLE_NAME} WHERE ${TransazioneType.COL_IMPORTO_DATO} = ${TransazioneType.COL_IMPORTO_TOTALE} AND ${TransazioneType.COL_DATAESTINZIONE} >= date('now', '-${numGiorni} days')")
        return getTransazioni("SELECT * FROM ${TransazioneType.TABLE_NAME} WHERE ${TransazioneType.COL_IMPORTO_DATO} = ${TransazioneType.COL_IMPORTO_TOTALE}")
    }

    fun getTransazioniAttive(tipo: Int): MutableList<TransazioneType> {
        val query =
            "SELECT * FROM ${TransazioneType.TABLE_NAME} WHERE ${TransazioneType.COL_TIPO} = ${tipo} AND ${TransazioneType.COL_IMPORTO_DATO} < ${TransazioneType.COL_IMPORTO_TOTALE}"
        return getTransazioni(query)
    }

    fun updateTransazione(transazione: TransazioneType) {
        val db = this.writableDatabase
        db.update(
            TransazioneType.TABLE_NAME,
            getTransContentValues(transazione),
            "${TransazioneType.COL_ID}= ${transazione.id}",
            null
        );
    }

    fun deleteTransazione(transazione: TransazioneType) {
        getNotifiche(transazione).forEach { deleteNotifica(it) }
        this.writableDatabase.delete(
            TransazioneType.TABLE_NAME,
            TransazioneType.COL_ID + "=" + transazione.id,
            null
        )
    }

    fun getCreditiAttivi(): MutableList<TransazioneType> {
        return getTransazioniAttive(TransazioneType.TIPO_CREDITO)
    }

    fun getDebitiAttivi(): MutableList<TransazioneType> {
        return getTransazioniAttive(TransazioneType.TIPO_DEBITO)
    }

    fun insertNotifiche(notifiche: ArrayList<NotificaType>, id: Int) {
        val database = this.writableDatabase
        notifiche.forEach {
            var contentValues = getNotifcontentValues(it, id)
            database.insert(NotificaType.TABLE_NAME, null, contentValues)
        }
    }

    private fun getNotifcontentValues(
        it: NotificaType,
        id: Int
    ): ContentValues {
        var contentValues = ContentValues()
        contentValues.put(NotificaType.COL_NUM_GIORNI_PRIMA, it.numGiorni)
        contentValues.put(NotificaType.COL_TRANSAZIONE_ID, id)
        return contentValues
    }

    fun getAllNotifiche(): MutableList<NotificaType> {
        val list: MutableList<NotificaType> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM ${NotificaType.TABLE_NAME}"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val notifica = NotificaType()
                notifica.id = result.getInt(result.getColumnIndex(NotificaType.COL_ID))
                notifica.numGiorni =
                    result.getInt(result.getColumnIndex(NotificaType.COL_NUM_GIORNI_PRIMA))
                notifica.idTransazione =
                    result.getInt(result.getColumnIndex(NotificaType.COL_TRANSAZIONE_ID))
                list.add(notifica)
            } while (result.moveToNext())
        }
        result.close()
        return list
    }

    fun getNotifiche(transazione: TransazioneType): MutableList<NotificaType> {
        val list: MutableList<NotificaType> = ArrayList()
        val db = this.readableDatabase
        val query =
            "SELECT * FROM ${NotificaType.TABLE_NAME} WHERE ${NotificaType.COL_TRANSAZIONE_ID} = ${transazione.id}"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val notifica = NotificaType()
                notifica.id = result.getInt(result.getColumnIndex(NotificaType.COL_ID))
                notifica.numGiorni =
                    result.getInt(result.getColumnIndex(NotificaType.COL_NUM_GIORNI_PRIMA))
                notifica.idTransazione =
                    result.getInt(result.getColumnIndex(NotificaType.COL_TRANSAZIONE_ID))
                list.add(notifica)
            } while (result.moveToNext())
        }
        result.close()
        return list
    }

    fun updateNotifiche(list: ArrayList<NotificaType>, id: Int) {
        this.writableDatabase.delete(
            NotificaType.TABLE_NAME,
            NotificaType.COL_TRANSAZIONE_ID + "=" + id,
            null
        )
        insertNotifiche(list, id)
    }

    fun deleteNotifica(notifica: NotificaType) {
        this.writableDatabase.delete(
            NotificaType.TABLE_NAME,
            NotificaType.COL_ID + "=" + notifica.id,
            null
        )
    }

    private fun getTotalImportTransaction(t: Int): Double {
        val db = this.readableDatabase
        val query =
            "SELECT SUM(${TransazioneType.COL_IMPORTO_TOTALE}) FROM ${TransazioneType.TABLE_NAME} WHERE ${TransazioneType.COL_TIPO} = ${t} AND ${TransazioneType.COL_IMPORTO_DATO} < ${TransazioneType.COL_IMPORTO_TOTALE}"
        val result = db.rawQuery(query, null)
        var imp = 0.00
        if (result.moveToFirst()) {
            imp = result.getDouble(0);
        }
        result.close()
        return imp
    }

    fun getTotalImportCredit(): Double {
        return getTotalImportTransaction(TransazioneType.TIPO_CREDITO)
    }

    fun getTotalImportDebit(): Double {
        return getTotalImportTransaction(TransazioneType.TIPO_DEBITO)
    }

    fun getTransazioniInScadenza(): List<TransazioneType> {
        val listTransazioniSetScadenza: ArrayList<TransazioneType> = ArrayList()
        var listTransazioniInScadenza: ArrayList<TransazioneType> = ArrayList()
        val db = this.readableDatabase
        val q1= "SELECT * FROM ${TransazioneType.TABLE_NAME} WHERE ${TransazioneType.COL_DATASCADENZA} <> ''"
        var result = db.rawQuery(q1, null)
        if (result.moveToFirst()) {
            do {
                val transazione = TransazioneType()
                transazione.id = result.getInt(result.getColumnIndex(TransazioneType.COL_ID))
                transazione.generalita =
                    result.getString(result.getColumnIndex(TransazioneType.COL_GENERALITA))
                transazione.descrizione =
                    result.getString(result.getColumnIndex(TransazioneType.COL_DESCRIZIONE))
                transazione.numeroTelefono =
                    result.getString(result.getColumnIndex(TransazioneType.COL_TELEFONO))
                transazione.dataInizio = fromDateDBTONormal(
                    result.getString(
                        result.getColumnIndex(
                            TransazioneType.COL_DATAINIZIO
                        )
                    )
                )
                transazione.dataFine = fromDateDBTONormal(
                    result.getString(
                        result.getColumnIndex(
                            TransazioneType.COL_DATASCADENZA
                        )
                    )
                )
                transazione.dataEstinzione = fromDateDBTONormal(
                    result.getString(
                        result.getColumnIndex(
                            TransazioneType.COL_DATAESTINZIONE
                        )
                    )
                )
                transazione.soldiTotali =
                    result.getDouble(result.getColumnIndex(TransazioneType.COL_IMPORTO_TOTALE))
                transazione.soldiRicevuti =
                    result.getDouble(result.getColumnIndex(TransazioneType.COL_IMPORTO_DATO))
                transazione.credito =
                    if (result.getInt(result.getColumnIndex(TransazioneType.COL_TIPO)) == TransazioneType.TIPO_CREDITO) true else false
                listTransazioniSetScadenza.add(transazione)
            } while (result.moveToNext())
        }
        listTransazioniSetScadenza.forEach {
            val listNotifiche = getNotifiche(it)
            val trans = it
            listNotifiche.forEach {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                var dataConfrontoDT = Date()
                val c: Calendar = Calendar.getInstance()
                c.setTime(dataConfrontoDT)
                c.add(Calendar.DATE, it.numGiorni)
                dataConfrontoDT = c.getTime()
                val dataConfrontoStr = sdf.format(dataConfrontoDT)
                if(trans.dataFine == dataConfrontoStr) {
                    listTransazioniInScadenza.add(trans)
                    //Rimuovo la notifica
                    deleteNotifica(it)
                }

            }
        }
        result.close()
        return listTransazioniInScadenza
    }

    companion object {
        const val DATABASE_VERSION = 3
        const val DATABASE_NAME = "QB_data.db"
    }


}