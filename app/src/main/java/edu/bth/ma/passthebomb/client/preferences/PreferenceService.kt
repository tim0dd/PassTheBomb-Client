package edu.bth.ma.passthebomb.client.preferences

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import java.util.*


const val PREF_UNIQUE_ID = "PREF_UNIQUE_ID"
const val LAST_DOWNLOAD_OVERVIEW = "LAST_DWN_OVERVIEW"
const val PLAYERS = "PLAYERS"

class PreferenceService constructor(private val context: Context) {
    var uniqueId: String? = null

    @SuppressLint("ApplySharedPref")
    fun getUniqueUserId(): String {
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

    fun getLastDownloadOverviewsDate(): Date {
        val sharedPrefs: SharedPreferences = context.getSharedPreferences(
            LAST_DOWNLOAD_OVERVIEW, Context.MODE_PRIVATE
        )
        return Date(sharedPrefs.getLong(LAST_DOWNLOAD_OVERVIEW, 0))

    }

    fun setLastDownloadOverviewsDate(date: Date) {
        val sharedPrefs: SharedPreferences = context.getSharedPreferences(
            LAST_DOWNLOAD_OVERVIEW, Context.MODE_PRIVATE
        )
        val editor = sharedPrefs.edit()
        editor.putLong(LAST_DOWNLOAD_OVERVIEW, date.time)
        editor.apply()
    }

    fun setPlayerNames(playerNames: Set<String>) {
        val sharedPrefs: SharedPreferences = context.getSharedPreferences(
            PLAYERS, Context.MODE_PRIVATE
        )
        val editor = sharedPrefs.edit()
        editor.putStringSet(PLAYERS, playerNames)
        editor.apply()
    }

    fun getPlayerNames(): Set<String> {
        val sharedPrefs: SharedPreferences = context.getSharedPreferences(
            PLAYERS, Context.MODE_PRIVATE
        )
        return sharedPrefs.getStringSet(PLAYERS, setOf<String>())!!
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