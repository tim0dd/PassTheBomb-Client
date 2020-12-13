package edu.bth.ma.passthebomb.client.remote

import android.content.Context
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import edu.bth.ma.passthebomb.client.database.AppDb
import edu.bth.ma.passthebomb.client.database.ChallengeRepository
import edu.bth.ma.passthebomb.client.database.ChallengeSetRepository
import edu.bth.ma.passthebomb.client.model.ChallengeSet
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview
import edu.bth.ma.passthebomb.client.preferences.PreferenceService
import java.util.*

// 10.0.2.2 maps to localhost when using android emulator
const val REST_URL = "http://192.168.0.101:8080"
const val API_GET_ALL = "/api/downloadOverviews"
const val API_GET = "/api/download?globalId="
const val API_UPLOAD = "/api/upload"
const val API_NEW_SETS = "/api/getNumberOfNewSets?="

class RestService constructor(private val context: Context) {
    private val challengeSetRepository: ChallengeSetRepository =
        AppDb.getDatabase(context).getChallengeSetRepository()

    val challengeRepository: ChallengeRepository =
        AppDb.getDatabase(context).getChallengeRepository()

    val queue = Volley.newRequestQueue(context)


    fun getChallengeSetOverviews(
        onSuccess: (result: List<ChallengeSetOverview>) -> Unit,
        onFail: (error: String) -> Unit
    ) {
        val request = JsonArrayRequest(Request.Method.GET,
            REST_URL + API_GET_ALL,
            null,
            { response ->
                run {
                    val now = Date(System.currentTimeMillis())
                    PreferenceService.getInstance(context).setLastDownloadOverviewsDate(now)
                    onSuccess(JsonConverters.jsonToChallengeSetOverviewList(response)!!)
                }
            },
            { error: VolleyError? -> (onFail(error.toString())) })
        queue.add(request)
    }


    fun getChallengeSet(
        id: String,
        onSuccess: (result: ChallengeSet) -> Unit,
        onFail: (error: String) -> Unit
    ) {
        val request = JsonObjectRequest(Request.Method.GET,
            REST_URL + API_GET + id,
            null,
            { response ->
                run {
                    val challengeSet = JsonConverters.jsonToChallengeSet(response)!!
                    onSuccess(challengeSet)
                    challengeSetRepository.addChallengeSetOverview(challengeSet.challengeSetOverview)
                    challengeSet.challenges.forEach { c -> challengeRepository.addChallenge(c) }
                }
            },
            { error: VolleyError? -> (onFail(error.toString())) })
        queue.add(request)
    }


    fun uploadChallengeSet(
        challengeSet: ChallengeSet,
        onSuccess: () -> Unit,
        onFail: (error: String) -> Unit
    ) {
        val request = JsonObjectRequest(Request.Method.POST,
            REST_URL + API_UPLOAD,
            JsonConverters.challengeSetToJson(challengeSet),
            { onSuccess() },
            { error: VolleyError? -> (onFail(error.toString())) })
        queue.add(request)
    }

    companion object {
        @Volatile
        private var INSTANCE: RestService? = null

        fun getInstance(context: Context): RestService {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = RestService(context)
                INSTANCE = instance
                return instance
            }
        }
    }
}