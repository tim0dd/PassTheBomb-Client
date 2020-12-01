package edu.bth.ma.passthebomb.client.viewmodel.challengesetlist

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import edu.bth.ma.passthebomb.client.data.Database
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview

class MyChallengeSetsVm() : ViewModel(), ChallengeSetListVm {
    override val challengeSetOverviews: ArrayList<ChallengeSetOverview> = Database().loadLocalChallengeSetOverviews()
    override fun onChallengeSetClick(index: Int) {

    }

    override fun onButton(activity: AppCompatActivity) {
        TODO("Not yet implemented")
    }
}
