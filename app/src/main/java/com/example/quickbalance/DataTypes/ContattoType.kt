package com.example.quickbalance.DataTypes

import android.os.Parcel
import android.os.Parcelable

data class ContattoType(var generalita:String?, var numeroTelefono:String?, var selezionato:Boolean?) : Parcelable {
    constructor(parcel: Parcel) : this(
        generalita = parcel.readString(),
        numeroTelefono = parcel.readString(),
        selezionato = parcel.readString()?.toBoolean()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(generalita)
        parcel.writeString(numeroTelefono)
        parcel.writeString(selezionato.toString())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PartecipanteType> {
        override fun createFromParcel(parcel: Parcel): PartecipanteType {
            return PartecipanteType(parcel)
        }

        override fun newArray(size: Int): Array<PartecipanteType?> {
            return arrayOfNulls(size)
        }
    }
}