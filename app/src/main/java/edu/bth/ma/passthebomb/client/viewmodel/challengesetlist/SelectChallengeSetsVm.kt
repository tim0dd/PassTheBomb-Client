package edu.bth.ma.passthebomb.client.viewmodel.challengesetlist

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bth.ma.passthebomb.client.database.MockDatabase
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview
import edu.bth.ma.passthebomb.client.view.GameSettingsActivity

class SelectChallengeSetsVm(application: Application) :ChallengeSetListVm(application) {
    val selectedChallengeSetIndices = MutableLiveData<HashSet<Int>>(HashSet<Int>())

    override fun init(challengeSets: List<ChallengeSetOverview>){
        super.init(challengeSets)
        selectedChallengeSetIndices.value?.clear()
    }

    override fun onChallengeSetClick(index: Int, context: Context) {
        val set: HashSet<Int> = selectedChallengeSetIndices.value ?: HashSet<Int>()
        if(set.contains(index)){
            set.remove(index)
        }else{
            set.add(index)
        }
        selectedChallengeSetIndices.value = set
    }

    override fun onButton(activity: AppCompatActivity) {
        val challengeSetIds = ArrayList<String>()
        val set: HashSet<Int> = selectedChallengeSetIndices.value ?: HashSet<Int>()
        for(index in set){
            challengeSetIds.add(challengeSetOverviews[index].id)
        }
        val intent = Intent(activity, GameSettingsActivity::class.java)
        intent.putExtra("CHALLENGE_SET_IDS", challengeSetIds)
        activity.startActivity(intent)
    }
}
