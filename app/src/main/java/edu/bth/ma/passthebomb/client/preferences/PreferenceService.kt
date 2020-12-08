package edu.bth.ma.passthebomb.client.preferences

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import java.util.*


const val PREF_UNIQUE_ID = "PREF_UNIQUE_ID"

class PreferenceService constructor(private val context: Context) {
    private var uniqueID: String? = null

    init {
        getUniqueId()
    }


    @SuppressLint("ApplySharedPref")
    private fun getUniqueId() : String {
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
        return uniqueID as String
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