package edu.bth.ma.passthebomb.client.remote

import android.content.Context
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class RestService constructor(context: Context) {

    private val queue = Volley.newRequestQueue(context)
    private val localhostInEmulator = "http://10.0.2.2:8080"
    private val getAllAPI = "/api/challengeSets/getAll"
    private val uploadAPI = "/api/challengeSets/upload"


    fun getChallengeSets(onSuccess: (result: JSONArray) -> Unit, onFail: (error: String) -> Unit) {
        val request = JsonArrayRequest(Request.Method.GET,
            localhostInEmulator + getAllAPI,
            null,
            { response -> onSuccess(response) },
            { error: VolleyError? -> (onFail(error.toString())) })
        queue.add(request)
    }
    fun uploadChallengeSet(

    ) {
        //TODO download single challenge set
        // store directly into db
    }


    fun uploadChallengeSet(
        challengeSet: JSONObject,
        onSuccess: () -> Unit,
        onFail: (error: String) -> Unit
    ) {
        val request = JsonObjectRequest(Request.Method.POST,
            localhostInEmulator + uploadAPI,
            challengeSet,
            { response -> onSuccess() },
            { error: VolleyError? -> (onFail(error.toString())) })
        queue.add(request)
    }


    companion object {
        @Volatile
        private var INSTANCE: RestService? = null

        fun getRestService(context: Context): RestService {
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