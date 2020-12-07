package edu.bth.ma.passthebomb.client.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Intent
import edu.bth.ma.passthebomb.client.model.ChallengeSet
import edu.bth.ma.passthebomb.client.view.EditChallengeActivity

class ChallengeSetVm(application: Application) : DatabaseVm(application) {

    lateinit var challengeSet: ChallengeSet

    fun initChallengeSet(challengeSetId: String) {
        if (!this::challengeSet.isInitialized) {
            // TODO: kinda unsafe cast, can throw exception. Should be Int as param anyway
            val id = challengeSetId.toInt()
        /*    getChallengeSet(id).observeForever { challengeSet: ChallengeSet ->
                this.challengeSet = challengeSet
            }*/
        }
    }

    fun onCreateNewChallenge(context: Activity) {
        val intent = Intent(context, EditChallengeActivity::class.java)
        intent.putExtra("CHALLENGE_SET_ID", challengeSet.challengeSetOverview.id)
        intent.putExtra("CHALLENGE_ID", -1)
        context.startActivity(intent)
    }

    fun selectChallenge(index: Int, context: Activity) {
        val intent = Intent(context, EditChallengeActivity::class.java)
        intent.putExtra("CHALLENGE_SET_ID", challengeSet.challengeSetOverview.id)
        intent.putExtra("CHALLENGE_ID", index)
        context.startActivity(intent)
    }
}