package edu.bth.ma.passthebomb.client.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import edu.bth.ma.passthebomb.client.model.Challenge

class EditChallengeVm(application: Application) : DatabaseVm(application) {
    var challenge: MutableLiveData<Challenge> = MutableLiveData()

    fun init(challenge: Challenge) {
        if(this.challenge.value == null){
            this.challenge.value = challenge
        }
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