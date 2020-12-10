package edu.bth.ma.passthebomb.client.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bth.ma.passthebomb.client.database.MockDatabase
import edu.bth.ma.passthebomb.client.model.Challenge
import java.util.*

class EditChallengeVm : ViewModel() {
    var challenge: MutableLiveData<Challenge> = MutableLiveData()

    fun initChallenge(challengeSetId: String, challengeId: Int) {
        if (challengeId == -1) {
            val now = Date(System.currentTimeMillis())

            challenge.value = Challenge(0, "0", now, "First challenge text", 100)
        } else {
            challenge.value = MockDatabase().loadChallenge(challengeId)
        }
    }

    fun onSave(context: Activity) {
        MockDatabase().storeChallenge(challenge.value)
        context.finish()
    }

    fun onCancel(context: Activity) {
        context.finish()
    }

    fun setTimeLimit(timeLimit: Int) {
        val oldChallenge = challenge.value!!
        challenge.value = Challenge(
            oldChallenge.id,
            oldChallenge.challengeSetId,
            oldChallenge.createdDate,
            oldChallenge.text,
            timeLimit
        )
    }

    fun setChallengeText(challengeText: String) {
        val oldChallenge = challenge.value!!
        challenge.value = Challenge(
            oldChallenge.id,
            oldChallenge.challengeSetId,
            oldChallenge.createdDate,
            challengeText,
            oldChallenge.timeLimit
        )
    }


}