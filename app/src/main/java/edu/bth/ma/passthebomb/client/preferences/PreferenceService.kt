package edu.bth.ma.passthebomb.client.preferences

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import java.util.*


const val PREF_UNIQUE_ID = "PREF_UNIQUE_ID"

class PreferenceService constructor(private val context: Context) {
    var uniqueId: String? = null

    @SuppressLint("ApplySharedPref")
     fun getUniqueUserId() : String {
        if (uniqueId == null) {
            val sharedPrefs: SharedPreferences = context.getSharedPreferences(
                PREF_UNIQUE_ID, Context.MODE_PRIVATE
            )
            uniqueId = sharedPrefs.getString(PREF_UNIQUE_ID, null)
            if (uniqueId == null) {
                uniqueId = UUID.randomUUID().toString()
                val editor = sharedPrefs.edit()
                editor.putString(PREF_UNIQUE_ID, uniqueId)
                editor.commit()
            }
        }
        return uniqueId as String
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