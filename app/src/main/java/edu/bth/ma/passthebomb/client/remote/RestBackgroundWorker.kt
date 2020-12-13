package edu.bth.ma.passthebomb.client.remote

import android.content.Context
import androidx.work.*
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import edu.bth.ma.passthebomb.client.preferences.PreferenceService
import edu.bth.ma.passthebomb.client.utils.Notification.Companion.notifyNewChallengeSets
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

const val WORKER_TAG = "passthebomb-restbackgroundworker"
const val TIMEOUT_SECONDS: Long = 60
const val REPEAT_PERIOD_DAYS: Long = 3
const val NOTIFICATION_CHANNEL_ID = "passthebomb_notification_channel"

class RestBackgroundWorker(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        val lastDownloadOverviewsDate = PreferenceService(context).getLastDownloadOverviewsDate()
        val future: RequestFuture<JSONObject> = RequestFuture.newFuture()
        val request = JsonObjectRequest(
            Request.Method.GET,
            REST_URL + API_NEW_SETS + lastDownloadOverviewsDate.time,
            null,
            future,
            future
        )
        RestService.getInstance(applicationContext).queue.add(request)
        try {
            val response = future[TIMEOUT_SECONDS, TimeUnit.SECONDS]
            val numberOfNewSets = response.getInt("numberOfNewSets")
            if (numberOfNewSets > 0) {
                notifyNewChallengeSets(context, numberOfNewSets)
            }
            return Result.success()
        } catch (e: JSONException) {
            e.printStackTrace()
            return Result.failure()
        } catch (e: InterruptedException) {
            e.printStackTrace()
            return Result.failure()
        } catch (e: ExecutionException) {
            e.printStackTrace()
            return Result.failure()
        } catch (e: TimeoutException) {
            e.printStackTrace()
            return Result.failure()
        }
    }


    companion object {
        fun registerWork(context: Context) {
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORKER_TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                getWorkRequest()
            )
        }

        private fun getWorkRequest(): PeriodicWorkRequest {
            return PeriodicWorkRequestBuilder<RestBackgroundWorker>(
                REPEAT_PERIOD_DAYS,
                TimeUnit.DAYS
            ).addTag(WORKER_TAG).build()
        }
    }
}