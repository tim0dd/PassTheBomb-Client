package edu.bth.ma.passthebomb.client.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview
import edu.bth.ma.passthebomb.client.model.Challenge
import edu.bth.ma.passthebomb.client.model.ChallengeSet
import edu.bth.ma.passthebomb.client.remote.RestService
import edu.bth.ma.passthebomb.client.utils.IdGenerator
import edu.bth.ma.passthebomb.client.viewmodel.DatabaseVm
import java.util.*

class DbTestActivity : AppCompatActivity() {


    private lateinit var databaseVm: DatabaseVm

    val uuid = UUID.randomUUID().toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_main)
        databaseVm = ViewModelProvider(this).get(DatabaseVm::class.java)

        getSupportActionBar()?.hide()

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
        val now = Date(System.currentTimeMillis())
        val challenge1 = Challenge("0", uuid, now, "First challenge text", 100)
        val challenge2 = Challenge("1", uuid, now, "First challenge text", 100)
        val challenges = listOf(challenge1, challenge2)
        val overview = ChallengeSetOverview(uuid, "0", "Animals", now, now, null, now, 1337)
        databaseVm.addChallengeSet(ChallengeSet(overview, challenges))
        /*   databaseVm.addChallenge(challenge1)
           databaseVm.addChallenge(challenge2)*/
        Toast.makeText(this, "ChallengeSet added!", Toast.LENGTH_SHORT).show()
    }

    private fun getChallengeSet() {

        val liveData = databaseVm.getChallengeSet(uuid)
        liveData.observe(
            this,
            Observer { challengeSet: ChallengeSet? ->
                liveData.removeObservers(this)
                Toast.makeText(
                    this,
                    challengeSet.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            })
        val liveDataChallenge = databaseVm.getChallenge("0")
        liveDataChallenge.observe(
            this,
            Observer { challenge: Challenge? ->
                liveDataChallenge.removeObservers(this)
                Toast.makeText(
                    this,
                    challenge.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            })
    }

    private fun deleteChallengeSet() {
        val liveData = databaseVm.getChallengeSet(uuid)
        RestService.getInstance(this).getChallengeSetOverviews(
            { response: List<ChallengeSetOverview> ->
                Toast.makeText(
                    this,
                    response.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            },
            { Toast.makeText(this, "FAIL!", Toast.LENGTH_SHORT).show() })
        RestService.getInstance(this).getChallengeSet(uuid,
            { response: ChallengeSet ->
                Toast.makeText(
                    this,
                    response.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            },
            { Toast.makeText(this, "FAIL!", Toast.LENGTH_SHORT).show() })
        /*  liveData.observe(
              this,
              Observer { challengeSet: ChallengeSet? ->
                  if (challengeSet != null) {
                      liveData.removeObservers(this)
                      databaseVm.deleteChallengeSet(challengeSet)
                      Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show()
                  }
              })*/
    }

    private fun updateChallengeSet() {
        val liveData = databaseVm.getChallengeSet(uuid)
        liveData.observe(
            this,
            Observer { challengeSet: ChallengeSet? ->
                if (challengeSet == null) return@Observer
                RestService.getInstance(this).uploadChallengeSet(challengeSet, {}, {})
                liveData.removeObservers(this)
                val now = Date(System.currentTimeMillis())
                val overview = challengeSet.challengeSetOverview
                overview.name = "ALIENS"
                overview.modifiedDate = now
                val challenge = Challenge("2", overview.id, now, "Third challenge text", 100)
                val list = mutableListOf<Challenge>()
                list.addAll(challengeSet.challenges)
                list.add(challenge)
                databaseVm.updateChallengeSet(challengeSet)
                Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show()
            })
    }
}