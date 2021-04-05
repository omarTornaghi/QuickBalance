package com.example.quickbalance.DataTypes

data class PartecipanteType(var cognome:String, var nome:String, var numeroTelefono:String?) {
    public fun getGeneralita() : String
    {
        return cognome + " " + nome
    }
}