package edu.bth.ma.passthebomb.client.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModel
import edu.bth.ma.passthebomb.client.database.MockDatabase
import edu.bth.ma.passthebomb.client.model.ChallengeSet
import edu.bth.ma.passthebomb.client.view.EditChallengeActivity

class ChallengeSetVm: ViewModel() {

    lateinit var challengeSet: ChallengeSet

    fun initChallengeSet(challengeSetId: String){
        if(!this::challengeSet.isInitialized){
            challengeSet = MockDatabase().loadChallengeSet(challengeSetId)
        }
    }

    fun onCreateNewChallenge(context: Activity){
        val intent = Intent(context, EditChallengeActivity::class.java)
        intent.putExtra("CHALLENGE_SET_ID", challengeSet.id )
        intent.putExtra("CHALLENGE_ID", -1)
        context.startActivity(intent)
    }

    fun selectChallenge(index: Int, context: Activity){
        val intent = Intent(context, EditChallengeActivity::class.java)
        intent.putExtra("CHALLENGE_SET_ID", challengeSet.id )
        intent.putExtra("CHALLENGE_ID", index)
        context.startActivity(intent)
    }
}