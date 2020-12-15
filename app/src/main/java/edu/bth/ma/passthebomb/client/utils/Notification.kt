package edu.bth.ma.passthebomb.client.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.remote.NOTIFICATION_CHANNEL_ID
import edu.bth.ma.passthebomb.client.view.challengesetlist.DownloadChallengeSetActivity

const val CHANNEL_ID = "passthebomb_notification_channel"
const val NAME = "Pass the bomb"

class Notification {
    companion object {

        fun createNotificationChannel(context: Context) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // val name = getString(R.string.channel_name)
                //  val descriptionText = getString(R.string.channel_description)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, NAME, importance).apply {
                    description = "Pass the bomb"
                }
                // Register the channel with the system
                val notificationManager: NotificationManager? =
                    getSystemService(context, NotificationManager::class.java)
                notificationManager!!.createNotificationChannel(channel)
            }
        }

        fun notifyNewChallengeSets(context: Context, numberOfNewSets: Int) {
            val resultIntent = Intent(context, DownloadChallengeSetActivity::class.java)
            val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(resultIntent)
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }
            val builder =
                NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).setContentTitle(NAME)
                    .setContentIntent(resultPendingIntent)
                    .setContentText("There is $numberOfNewSets new challenge sets available!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    /*  .setStyle(NotificationCompat.BigTextStyle()
                          .bigText("Much longer text that cannot fit one line..."))*/
                    .setSmallIcon(R.drawable.ic_download)

            with(NotificationManagerCompat.from(context)) {
                notify(IdGenerator().getRandomIntId(), builder.build())
            }

        }
    }


}