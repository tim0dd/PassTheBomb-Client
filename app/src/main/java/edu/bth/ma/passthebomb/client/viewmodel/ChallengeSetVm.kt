package edu.bth.ma.passthebomb.client.viewmodel

import androidx.lifecycle.ViewModel
import edu.bth.ma.passthebomb.client.database.MockDatabase
import edu.bth.ma.passthebomb.client.model.ChallengeSet

class ChallengeSetVm: ViewModel() {

    lateinit var challengeSet: ChallengeSet

    fun initChallengeSet(challengeSetId: String){
        if(!this::challengeSet.isInitialized){
            challengeSet = MockDatabase().loadChallengeSet(challengeSetId)
        }
    }
}