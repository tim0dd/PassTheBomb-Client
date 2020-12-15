package edu.bth.ma.passthebomb.client.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import edu.bth.ma.passthebomb.client.model.ChallengeSet
import edu.bth.ma.passthebomb.client.utils.ObserveExtensions.Companion.observeOnce
import kotlin.collections.ArrayList

class ChallengeSetVm(application: Application) : DatabaseVm(application) {

    lateinit var challengeSetId: String
    val challengeSet = MutableLiveData<ChallengeSet>()

    fun init(challengeSetId: String){
        if(!this::challengeSetId.isInitialized){
            this.challengeSetId = challengeSetId
            getChallengeSet(challengeSetId).observeOnce {
                challengeSet.value = it
            }
        }
    }
}
