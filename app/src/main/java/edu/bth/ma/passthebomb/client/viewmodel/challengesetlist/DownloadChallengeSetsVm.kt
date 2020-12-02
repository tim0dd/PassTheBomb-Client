package edu.bth.ma.passthebomb.client.viewmodel.challengesetlist

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview
import edu.bth.ma.passthebomb.client.remote.MockRest

class DownloadChallengeSetsVm() : ViewModel(), ChallengeSetListVm {
    override val challengeSetOverviews: ArrayList<ChallengeSetOverview> = MockRest().loadOnlineChallengeSetOverviews()
    override fun onChallengeSetClick(index: Int, context: Context) {
        TODO("Not yet implemented")
    }

    override fun onButton(activity: AppCompatActivity) {}
}