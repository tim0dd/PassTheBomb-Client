package edu.bth.ma.passthebomb.client.viewmodel.challengesetlist

import android.app.Application
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import edu.bth.ma.passthebomb.client.utils.ObserveExtensions.Companion.observeOnce
import edu.bth.ma.passthebomb.client.view.GameSettingsActivity

class SelectChallengeSetsVm(application: Application) :ChallengeSetListVm(application) {
    val selectedChallengeSetIndices = MutableLiveData<HashSet<Int>>(HashSet<Int>())

    init{
        getAllChallengeSets().observeOnce{
            challengeSetOverviews.value = ArrayList(it)
         }

    }

    fun selectChallengeSet(index: Int) {
        val set: HashSet<Int> = selectedChallengeSetIndices.value ?: HashSet<Int>()
        if(set.contains(index)){
            set.remove(index)
        }else{
            set.add(index)
        }
        selectedChallengeSetIndices.value = set
    }
}
