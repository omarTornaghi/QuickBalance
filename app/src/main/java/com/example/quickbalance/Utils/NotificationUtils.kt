package com.example.quickbalance.Utils

import android.content.Context

class NotificationUtils {
    companion object{
            private val NAME_SHARE_PREF = "PREFERENZE_NOTIFICHE"
            private val KEY_SEND_NOTIFICATIONS = "inviareNotifiche"

            fun notificationsEnabled(context:Context):Boolean{
                val sharedPref = context.getSharedPreferences(NAME_SHARE_PREF, Context.MODE_PRIVATE)
                val notEn = sharedPref.getBoolean(KEY_SEND_NOTIFICATIONS, true)
                return notEn
            }

            fun saveNotificationSetting(context: Context, state:Boolean){
                val sharedPref = context.getSharedPreferences(NAME_SHARE_PREF, Context.MODE_PRIVATE) ?: return
                with (sharedPref.edit()) {
                    putBoolean(KEY_SEND_NOTIFICATIONS, state)
                    apply()
                }
            }

    }
}