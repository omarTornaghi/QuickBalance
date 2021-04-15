package com.example.quickbalance.DataTypes

data class NotificaType(var descrizione:String){
    var numGiorni:Int = 1


    companion object{
        fun getDescrizione(num:Int):String{
            if(num == 1)
                return num.toString() + " day before"
            return num.toString() + " days before"
        }
    }
}