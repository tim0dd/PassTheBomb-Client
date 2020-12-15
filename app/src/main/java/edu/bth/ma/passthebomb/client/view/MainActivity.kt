package edu.bth.ma.passthebomb.client.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.preferences.PreferenceService
import edu.bth.ma.passthebomb.client.remote.RestBackgroundWorker
import edu.bth.ma.passthebomb.client.utils.Notification
import edu.bth.ma.passthebomb.client.view.challengesetlist.DownloadChallengeSetActivity
import edu.bth.ma.passthebomb.client.view.challengesetlist.MyChallengeSetsActivity
import edu.bth.ma.passthebomb.client.view.challengesetlist.SelectChallengeSetsActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_main)

        //disable night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        //initialize singletons
        PreferenceService(this)

        //initialize notification channel
        Notification.createNotificationChannel(this)

        //initialize rest notification worker
        RestBackgroundWorker.registerWork(this)

        supportActionBar?.hide();

        val buttonMyChallengeSets: Button = findViewById(R.id.buttonMyChallengeSets)
        buttonMyChallengeSets.setOnClickListener {
            val intent = Intent(this, MyChallengeSetsActivity::class.java)
            startActivity(intent)
        }
        val buttonDownloadChallengeSets: Button = findViewById(R.id.buttonDownloadChallengeSets)
        buttonDownloadChallengeSets.setOnClickListener {
            val intent = Intent(this, DownloadChallengeSetActivity::class.java)
            startActivity(intent)
        }
        val buttonStartGame: Button = findViewById(R.id.buttonStartGame)
        buttonStartGame.setOnClickListener {
            val intent = Intent(this, SelectChallengeSetsActivity::class.java)
            startActivity(intent)
        }
        val buttonTutorial: Button = findViewById(R.id.buttonTutorialAndRules)
        buttonTutorial.setOnClickListener {
            val intent = Intent(this, TutorialActivity::class.java)
            startActivity(intent)
        }
    }
}