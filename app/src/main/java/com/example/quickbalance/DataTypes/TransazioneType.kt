package com.example.quickbalance.DataTypes

import android.os.Parcel
import android.os.Parcelable

data class TransazioneType(var id:Int,var generalita: String?, var descrizione: String?, var soldiTotali:Double, var soldiRicevuti:Double, var numeroTelefono:String?, var dataInizio:String?, var dataFine:String?, var dataEstinzione:String?, var credito:Boolean, var espanso:Boolean) : Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readInt(),
        generalita = parcel.readString(),
        descrizione = parcel.readString(),
        soldiTotali = parcel.readDouble(),
        soldiRicevuti = parcel.readDouble(),
        numeroTelefono = parcel.readString(),
        dataInizio = parcel.readString(),
        dataFine = parcel.readString(),
        dataEstinzione = parcel.readString(),
        credito = parcel.readByte() != 0.toByte(),
        espanso = parcel.readByte() != 0.toByte()
    )

    constructor() : this(0,"","",0.00,0.00,"","","","",true,false)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(generalita)
        parcel.writeString(descrizione)
        parcel.writeDouble(soldiTotali)
        parcel.writeDouble(soldiRicevuti)
        parcel.writeString(numeroTelefono)
        parcel.writeString(dataInizio)
        parcel.writeString(dataFine)
        parcel.writeString(dataEstinzione)
        parcel.writeByte(if (credito) 1 else 0)
        parcel.writeByte(if (espanso) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TransazioneType> {
        override fun createFromParcel(parcel: Parcel): TransazioneType {
            return TransazioneType(parcel)
        }

        override fun newArray(size: Int): Array<TransazioneType?> {
            return arrayOfNulls(size)
        }

        val TABLE_NAME = "Transazione"
        val COL_ID = "id"
        val COL_GENERALITA = "generalita"
        val COL_DESCRIZIONE = "descrizione"
        val COL_TELEFONO = "telefono"
        val COL_DATAINIZIO = "dataInizio"
        val COL_DATASCADENZA = "dataScadenza"
        val COL_DATAESTINZIONE = "dataEstinzione"
        val COL_IMPORTO_TOTALE = "importoTotale"
        val COL_IMPORTO_DATO = "importoDato"
        val COL_TIPO = "tipo"
        val TIPO_CREDITO = 1
        val TIPO_DEBITO = 0

        val QUERY_CREAZIONE_TABLE = "CREATE TABLE $TABLE_NAME(\n" +
                "\t$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t$COL_GENERALITA VARCHAR(50),\n" +
                "\t$COL_DESCRIZIONE VARCHAR(100),\n" +
                "\t$COL_TELEFONO varchar(15),\n" +
                "\t$COL_DATAINIZIO DATE NOT NULL,\n" +
                "\t$COL_DATASCADENZA DATE,\n" +
                "\t$COL_DATAESTINZIONE DATE,\n" +
                "\t$COL_IMPORTO_TOTALE DECIMAL NOT NULL,\n" +
                "\t$COL_IMPORTO_DATO DECIMAL NOT NULL,\n" +
                "\t$COL_TIPO INTEGER CHECK($COL_TIPO BETWEEN 0 AND 1)" +
                "\t\n" +
                ");"

        val QUERY_CANCELLAZIONE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"

    }
}