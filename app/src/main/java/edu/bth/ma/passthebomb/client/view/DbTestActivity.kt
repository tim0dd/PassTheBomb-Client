package edu.bth.ma.passthebomb.client.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.database.ChallengeSetEntity
import edu.bth.ma.passthebomb.client.model.Challenge
import edu.bth.ma.passthebomb.client.view.challengesetlist.DownloadChallengeSetActivity
import edu.bth.ma.passthebomb.client.view.challengesetlist.MyChallengeSetsActivity
import edu.bth.ma.passthebomb.client.view.challengesetlist.SelectChallengeSetsActivity
import edu.bth.ma.passthebomb.client.viewmodel.DatabaseVm
import java.util.*

class DbTestActivity : AppCompatActivity() {


    private lateinit var databaseVm: DatabaseVm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_main)
        databaseVm = ViewModelProvider(this).get(DatabaseVm::class.java)

        getSupportActionBar()?.hide();

        val buttonMyChallengeSets: Button = findViewById(R.id.buttonMyChallengeSets)
        buttonMyChallengeSets.text = "Add ChallengeSet"
        buttonMyChallengeSets.setOnClickListener {
            addChallengeSet();
        }
        val buttonDownloadChallengeSets: Button = findViewById(R.id.buttonDownloadChallengeSets)
        buttonDownloadChallengeSets.text = "Delete ChallengeSet"
        buttonDownloadChallengeSets.setOnClickListener {
            deleteChallengeSet()
        }
        val buttonStartGame: Button = findViewById(R.id.buttonStartGame)
        buttonStartGame.text = "Update ChallengeSet"
        buttonStartGame.setOnClickListener {
            updateChallengeSet()

        }
        val buttonTutorial: Button = findViewById(R.id.buttonTutorialAndRules)
        buttonTutorial.text = "Get ChallengeSet"
        buttonTutorial.setOnClickListener {
            getChallengeSet()
        }
    }

    private fun addChallengeSet() {
        val challenge1 = Challenge("First Challenge", "First challenge text", 100)
        val challenge2 = Challenge("First Challenge", "First challenge text", 100)
        val challengeList = listOf(challenge1, challenge2)
        val now = Date(System.currentTimeMillis())
        val challengeSet =
            ChallengeSetEntity(0, 0, "Animals", now, now, now, 1337, challengeList)
        databaseVm.addChallengeSet(challengeSet)
        Toast.makeText(this, "ChallengeSet added!", Toast.LENGTH_SHORT).show()
    }

    private fun getChallengeSet() {
        val challengeSet = databaseVm.getChallengeSet(0)
        Toast.makeText(this, challengeSet.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun deleteChallengeSet() {
        val challengeSet = databaseVm.getChallengeSet(0)
        if (challengeSet != null) {
            databaseVm.deleteChallengeSet(challengeSet)
            Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show()
        } else Toast.makeText(this, "ChallengeSet not found!", Toast.LENGTH_SHORT)
            .show()

    }

    private fun updateChallengeSet() {
        val challengeSet = databaseVm.getChallengeSet(0)

        if (challengeSet != null) {
            challengeSet.name = "ALIENS"
            challengeSet.modifiedDate = Date(System.currentTimeMillis())
            challengeSet.downloads++
            databaseVm.updateChallengeSet(challengeSet)
            Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show()
        } else Toast.makeText(this, "ChallengeSet not found!", Toast.LENGTH_SHORT)
            .show()
    }
}