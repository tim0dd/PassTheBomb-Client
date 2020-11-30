package edu.bth.ma.passthebomb.client.viewmodel.challengesetlist

import androidx.lifecycle.ViewModel
import edu.bth.ma.passthebomb.client.data.Rest
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview

class DownloadChallengeSetsVm() : ViewModel(), ChallengeSetListVm {
    override val challengeSetOverviews: ArrayList<ChallengeSetOverview> = Rest().loadOnlineChallengeSetOverviews()
    override fun onChallengeSetClick(index: Int) {
        TODO("Not yet implemented")
    }

    override fun onButton() {}
}