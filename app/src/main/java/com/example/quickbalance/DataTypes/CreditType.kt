package com.example.quickbalance.DataTypes

import java.io.Serializable
import java.time.LocalDate
import java.util.*

data class CreditType(val generalita:String, val descrizione:String, val soldiTotali:Double, val soldiRicevuti:Double, val numeroTelefono:String, val dataInizio:String, val dataFine:String?, val credito:Boolean) : Serializable {
}