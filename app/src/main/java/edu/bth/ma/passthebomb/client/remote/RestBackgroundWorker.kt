package edu.bth.ma.passthebomb.client.remote

import android.content.Context
import androidx.work.*
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import edu.bth.ma.passthebomb.client.preferences.PreferenceService
import edu.bth.ma.passthebomb.client.utils.Notification.Companion.notifyNewChallengeSets
import org.json.JSONObject
import java.util.concurrent.TimeUnit

const val WORKER_TAG = "passthebomb-restbackgroundworker"
const val TIMEOUT_SECONDS: Long = 60
const val REPEAT_PERIOD_MINUTES: Long = 16
const val NOTIFICATION_CHANNEL_ID = "passthebomb_notification_channel"

class RestBackgroundWorker(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        val preferenceService = PreferenceService(context)
        val lastDownloadOverviews = preferenceService.getLastDownloadOverviewsDate().time
        val myUserId = preferenceService.getUniqueUserId()
        val future: RequestFuture<JSONObject> = RequestFuture.newFuture()
        val request = JsonObjectRequest(
            Request.Method.GET,
            REST_URL + API_NEW_SETS + API_NEW_SETS_USER_ID + myUserId + API_NEW_SETS_DWN_DATE + lastDownloadOverviews,
            null,
            future,
            future
        )
        return try {
            RestService.getInstance(applicationContext).queue.add(request)
            val response = future[TIMEOUT_SECONDS, TimeUnit.SECONDS]
            val numberOfNewSets = response.getInt("response")
            if (numberOfNewSets > 0) {
                notifyNewChallengeSets(context, numberOfNewSets)
            }
            Result.success()
        } catch (e: Exception) {
            // e.printStackTrace()
            Result.failure()
        }
    }


    companion object {
        fun registerWork(context: Context) {
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORKER_TAG,
                ExistingPeriodicWorkPolicy.REPLACE,
                getWorkRequest()
            )
        }

        private fun getWorkRequest(): PeriodicWorkRequest {
            return PeriodicWorkRequestBuilder<RestBackgroundWorker>(
                REPEAT_PERIOD_MINUTES,
                TimeUnit.MINUTES
            ).addTag(WORKER_TAG).build()
        }
    }
}