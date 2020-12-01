package edu.bth.ma.passthebomb.client.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bth.ma.passthebomb.client.data.Database
import edu.bth.ma.passthebomb.client.model.ChallengeSet

class ChallengeSetVm: ViewModel() {

    lateinit var challengeSet: ChallengeSet

    fun initChallengeSet(challengeSetId: String){
        if(!this::challengeSet.isInitialized){
            challengeSet = Database().loadChallengeSet(challengeSetId)
        }
    }
}