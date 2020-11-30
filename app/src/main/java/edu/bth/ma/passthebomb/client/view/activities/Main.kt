package edu.bth.ma.passthebomb.client.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.view.activities.challengesetlist.ChallengeSetList
import edu.bth.ma.passthebomb.client.view.activities.challengesetlist.DownloadChallengeList
import edu.bth.ma.passthebomb.client.view.activities.challengesetlist.MyChallengeSetList

class Main : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        val buttonMyChallengeSets: Button = findViewById(R.id.buttonMyChallengeSets)
        buttonMyChallengeSets.setOnClickListener {
            val intent = Intent(this, MyChallengeSetList::class.java).apply {  }
            startActivity(intent)
        }
        val buttonDownloadChallengeSets: Button = findViewById(R.id.buttonDownloadChallengeSets)
        buttonDownloadChallengeSets.setOnClickListener {
            val intent = Intent(this, DownloadChallengeList::class.java).apply {  }
            startActivity(intent)
        }
    }
}