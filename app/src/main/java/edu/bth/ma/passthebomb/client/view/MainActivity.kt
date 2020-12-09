package edu.bth.ma.passthebomb.client.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.database.AppDb
import edu.bth.ma.passthebomb.client.preferences.PreferenceService
import edu.bth.ma.passthebomb.client.view.challengesetlist.DownloadChallengeSetActivity
import edu.bth.ma.passthebomb.client.view.challengesetlist.MyChallengeSetsActivity
import edu.bth.ma.passthebomb.client.view.challengesetlist.SelectChallengeSetsActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_main)

        //initialize singletons
        PreferenceService(this)

        getSupportActionBar()?.hide();

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