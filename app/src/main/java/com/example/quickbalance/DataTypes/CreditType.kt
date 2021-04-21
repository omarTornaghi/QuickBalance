package com.example.quickbalance.DataTypes

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class CreditType(val generalita: String?, val descrizione: String?, val soldiTotali:Double, val soldiRicevuti:Double, val numeroTelefono:String?, val dataInizio:String?, val dataFine:String?, val credito:Boolean, var espanso:Boolean) : Parcelable {
    constructor(parcel: Parcel) : this(
        generalita = parcel.readString(),
        descrizione = parcel.readString(),
        soldiTotali = parcel.readDouble(),
        soldiRicevuti = parcel.readDouble(),
        numeroTelefono = parcel.readString(),
        dataInizio = parcel.readString(),
        dataFine = parcel.readString(),
        credito = parcel.readByte() != 0.toByte(),
        espanso = parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(generalita)
        parcel.writeString(descrizione)
        parcel.writeDouble(soldiTotali)
        parcel.writeDouble(soldiRicevuti)
        parcel.writeString(numeroTelefono)
        parcel.writeString(dataInizio)
        parcel.writeString(dataFine)
        parcel.writeByte(if (credito) 1 else 0)
        parcel.writeByte(if (espanso) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CreditType> {
        override fun createFromParcel(parcel: Parcel): CreditType {
            return CreditType(parcel)
        }

        override fun newArray(size: Int): Array<CreditType?> {
            return arrayOfNulls(size)
        }
    }
}