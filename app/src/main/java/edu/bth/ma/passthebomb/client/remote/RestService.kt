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

class RestService constructor(context: Context) {
    private val challengeSetRepository: ChallengeSetRepository =
        AppDb.getDatabase(context).getChallengeSetRepository()

    val challengeRepository: ChallengeRepository =
        AppDb.getDatabase(context).getChallengeRepository()

    private val queue = Volley.newRequestQueue(context)

    // 10.0.2.2 maps to localhost when using android emulator
    private val localhostInEmulator = "http://10.0.2.2:8080"
    private val getAllAPI = "/api/downloadOverviews"
    private val getAPI = "/api/download?globalId="
    private val uploadAPI = "/api/upload"


    fun getChallengeSetOverviews(
        onSuccess: (result: List<ChallengeSetOverview>) -> Unit,
        onFail: (error: String) -> Unit
    ) {
        val request = JsonArrayRequest(Request.Method.GET,
            localhostInEmulator + getAllAPI,
            null,
            { response -> onSuccess(JsonConverters.jsonToChallengeSetOverviewList(response)!!) },
            { error: VolleyError? -> (onFail(error.toString())) })
        queue.add(request)
    }


    fun getChallengeSet(
        id: String,
        onSuccess: (result: ChallengeSet) -> Unit,
        onFail: (error: String) -> Unit
    ) {
        val request = JsonObjectRequest(Request.Method.GET,
            localhostInEmulator + getAPI + id,
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
            localhostInEmulator + uploadAPI,
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