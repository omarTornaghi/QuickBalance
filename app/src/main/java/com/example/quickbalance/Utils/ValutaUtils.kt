package com.example.quickbalance.Utils

import android.app.Activity
import android.content.Context
import java.util.*

class ValutaUtils {
    companion object{
        private val NAME_SHARE_PREF = "VALUTA"
        private val KEY_CURRENCY_CODE = "valutaSelezionata"

        fun getSelectedCurrencyCode(activity:Activity):String{
            val sharedPref = activity.getSharedPreferences(NAME_SHARE_PREF, Context.MODE_PRIVATE)
            val codiceValutaSelezionata = sharedPref.getString(KEY_CURRENCY_CODE, "")
            return if(codiceValutaSelezionata == null)"" else codiceValutaSelezionata
        }

        fun getCurrencyCodeLocale():String{
            val cur: Currency = Currency.getInstance(Locale.getDefault())
            return cur.currencyCode
        }

        fun getSelectedCurrencySymbol(activity: Activity):String{
            val cur: Currency = Currency.getInstance(getSelectedCurrencyCode(activity))
            return cur.symbol
        }

        fun saveCurrencyCode(activity: Activity, currencyCode:String){
            val sharedPref = activity.getSharedPreferences(NAME_SHARE_PREF, Context.MODE_PRIVATE) ?: return
            with (sharedPref.edit()) {
                putString(KEY_CURRENCY_CODE, currencyCode)
                apply()
            }
        }
    }
}