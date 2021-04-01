package com.example.quickbalance.DataTypes

data class DestinatarioType(var cognome:String, var nome:String, var numeroTelefono:String?) {
    public fun getGeneralita() : String
    {
        return cognome + " " + nome
    }
}