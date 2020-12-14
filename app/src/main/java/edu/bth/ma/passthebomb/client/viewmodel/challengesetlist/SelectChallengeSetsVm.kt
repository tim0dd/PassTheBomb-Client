package edu.bth.ma.passthebomb.client.viewmodel.challengesetlist

import android.app.Application
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import edu.bth.ma.passthebomb.client.view.GameSettingsActivity

class SelectChallengeSetsVm(application: Application) :ChallengeSetListVm(application) {
    val selectedChallengeSetIndices = MutableLiveData<HashSet<Int>>(HashSet<Int>())

    override fun init(context: AppCompatActivity){
        getAllChallengeSets().observe(context,
            Observer {
                challengeSetOverviews.value = ArrayList(it)
            })
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
        if((selectedChallengeSetIndices.value?.size ?: 0) == 0){
            Toast.makeText(
                activity,
                "Please select at least one challenge set.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if(challengeSetOverviews.value!=null){
            val challengeSetIds = ArrayList<String>()
            val set: HashSet<Int> = selectedChallengeSetIndices.value ?: HashSet<Int>()
            for(index in set){
                challengeSetIds.add(challengeSetOverviews.value!![index].id)
            }
            val intent = Intent(activity, GameSettingsActivity::class.java)
            intent.putExtra("CHALLENGE_SET_IDS", challengeSetIds)
            activity.startActivity(intent)
        }
    }
}
