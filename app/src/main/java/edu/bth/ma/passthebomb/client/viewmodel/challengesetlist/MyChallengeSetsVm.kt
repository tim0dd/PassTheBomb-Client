package edu.bth.ma.passthebomb.client.viewmodel.challengesetlist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import edu.bth.ma.passthebomb.client.database.MockDatabase
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview
import edu.bth.ma.passthebomb.client.view.ChallengeSetActivity

class MyChallengeSetsVm() : ViewModel(), ChallengeSetListVm {
    override val challengeSetOverviews: ArrayList<ChallengeSetOverview> = MockDatabase().loadLocalChallengeSetOverviews()
    override fun onChallengeSetClick(index: Int, context: Context){
        val intent = Intent(context, ChallengeSetActivity::class.java)
        intent.putExtra("CHALLENGE_SET_ID", challengeSetOverviews[index].id)
        context.startActivity(intent)
    }

    override fun onButton(activity: AppCompatActivity) {
        TODO("Not yet implemented")
    }
}
