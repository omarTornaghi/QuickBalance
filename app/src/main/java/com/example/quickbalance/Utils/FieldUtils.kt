package com.example.quickbalance.Utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class FieldUtils {
    companion object {
        fun controllaNumero(telefono: String): Boolean {
            val m: Matcher
            val pattern =
                "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$"
            val r: Pattern = Pattern.compile(pattern)
            m = r.matcher(telefono.trim())
            if ((telefono.isBlank() || m.find())) {
                if (telefono.isBlank() || (telefono.get(0) != '+' && telefono.length == 10))
                    return true
                if (telefono.isBlank() || (telefono.get(0) == '+' && telefono.length == 13))
                    return true
            }
            return false
        }
        fun controllaDate(primaDataStr: String, secondaDataStr: String, format:String):Boolean{
            val sdf = SimpleDateFormat(format)
            val primaData: Date = sdf.parse(primaDataStr)
            val secondaData:Date = sdf.parse(secondaDataStr)
            if (secondaData.after(primaData))
                return true
            return false
        }
        fun normalizzaStringa(sIn: String):String{
            return sIn.trim().capitalize()
        }
    }
}