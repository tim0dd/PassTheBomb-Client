package edu.bth.ma.passthebomb.client.viewmodel

import android.app.Activity
import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import edu.bth.ma.passthebomb.client.model.Challenge

class EditChallengeVm(application: Application) : DatabaseVm(application) {
    var challenge: MutableLiveData<Challenge> = MutableLiveData()

    fun init(challenge: Challenge) {
        this.challenge.value = challenge
    }

    fun onSave(context: Activity) {
        if(challenge.value!=null){
            if(challenge.value!!.text != ""){
                updateChallenge(challenge.value!!)
            }else{
                Toast.makeText(context,"No challenge text, deleting challenge!",Toast.LENGTH_SHORT).show();
                deleteChallenge(challenge.value!!.id)
            }
        }
        context.finish()
    }

    fun onCancel(context: Activity) {
        if(challenge.value!=null && challenge.value!!.text == ""){
            deleteChallenge(challenge.value!!.id)
        }
        context.finish()
    }

    fun onDelete(context: Activity){
        if(challenge.value!=null){
            deleteChallenge(challenge.value!!.id)
        }
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