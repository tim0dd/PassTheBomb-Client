package edu.bth.ma.passthebomb.client.utils

import android.content.Context
import edu.bth.ma.passthebomb.client.preferences.PreferenceService
import java.util.*

class IdGenerator {

    fun generateDbId(): String {
        return UUID.randomUUID().toString()
    }

    fun getUserId(context: Context): String {
        val preferenceService = PreferenceService.getInstance(context)
        return preferenceService.getUniqueUserId()
    }

    fun getRandomIntId(): Int {
        return (1000000..9999999).random()
    }
}