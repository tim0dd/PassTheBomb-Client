package edu.bth.ma.passthebomb.client.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bth.ma.passthebomb.client.data.Database
import edu.bth.ma.passthebomb.client.model.Challenge
import edu.bth.ma.passthebomb.client.model.MAXIMUM_CHALLENGE_TIME

class EditChallengeVm: ViewModel() {
    lateinit var challenge: MutableLiveData<Challenge>

    fun initChallenge(challengeSetId: String,challengeId:String){
        if(!this::challenge.isInitialized){
            challenge.value = Database().loadChallenge(challengeSetId, challengeId)
        }
    }

    fun onTimeSlider(progress: Int){
        challenge.value?.timeLimit = progress
    }


    fun onSave(context: Activity){
        Database().storeChallenge(challenge.value)
        context.finish()
    }

    fun onCancel(context: Activity){
        context.finish()
    }


}