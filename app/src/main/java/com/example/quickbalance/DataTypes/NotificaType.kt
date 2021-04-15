package com.example.quickbalance.DataTypes

import android.content.Context
import com.example.quickbalance.R


data class NotificaType(var numGiorni:Int, val context:Context){
    override fun toString(): String {
        when(numGiorni){
            1->return "$numGiorni ${context.getString(R.string.day_before)}"
            else-> return "$numGiorni ${context.getString(R.string.days_before)}"
        }
    }
}