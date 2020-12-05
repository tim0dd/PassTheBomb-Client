package edu.bth.ma.passthebomb.client.preferences

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import java.util.*


class PreferenceService constructor(context: Context) {
    private var uniqueID: String? = null
    private val PREF_UNIQUE_ID = "PREF_UNIQUE_ID"

    init {
        initID(context)
    }


    //TODO functions for get / set game settings

    @SuppressLint("ApplySharedPref")
    private fun initID(context: Context) {
        if (uniqueID == null) {
            val sharedPrefs: SharedPreferences = context.getSharedPreferences(
                PREF_UNIQUE_ID, Context.MODE_PRIVATE
            )
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null)
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString()
                val editor = sharedPrefs.edit()
                editor.putString(PREF_UNIQUE_ID, uniqueID)
                editor.commit()
            }
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: PreferenceService? = null

        fun getInstance(context: Context): PreferenceService {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = PreferenceService(context)
                INSTANCE = instance
                return instance
            }
        }
    }

}