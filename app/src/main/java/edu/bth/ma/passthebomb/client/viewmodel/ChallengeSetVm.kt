package edu.bth.ma.passthebomb.client.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bth.ma.passthebomb.client.data.Database
import edu.bth.ma.passthebomb.client.model.ChallengeSet
import edu.bth.ma.passthebomb.client.view.EditChallengeActivity

class ChallengeSetVm: ViewModel() {

    lateinit var challengeSet: ChallengeSet

    fun initChallengeSet(challengeSetId: String){
        if(!this::challengeSet.isInitialized){
            challengeSet = Database().loadChallengeSet(challengeSetId)
        }
    }

    fun selectChallenge(challengeIndex: Int, context: Activity){
        val intent = Intent(context, EditChallengeActivity::class.java)
        intent.putExtra("CHALLENGE_SET_ID", challengeSet.id)
        intent.putExtra("CHALLENGE_ID", challengeSet.challenges[challengeIndex].id)
        context.startActivity(intent)
    }

    fun onCreateNewChallenge(context: Activity){
        val intent = Intent(context, EditChallengeActivity::class.java)
        intent.putExtra("CHALLENGE_SET_ID", challengeSet.id)
        intent.putExtra("CHALLENGE_ID", "")
        context.startActivity(intent)
    }
}