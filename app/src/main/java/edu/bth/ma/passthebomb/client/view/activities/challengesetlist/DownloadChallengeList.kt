package edu.bth.ma.passthebomb.client.view.activities.challengesetlist

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.view.adapter.ChallengeSetsAdapter
import edu.bth.ma.passthebomb.client.view.adapter.DownloadChallengeSetsAdapter

import edu.bth.ma.passthebomb.client.view.adapter.MyChallengeSetsAdapter
import edu.bth.ma.passthebomb.client.viewmodel.challengesetlist.ChallengeSetListVm
import edu.bth.ma.passthebomb.client.viewmodel.challengesetlist.DownloadChallengeSetsVm
import edu.bth.ma.passthebomb.client.viewmodel.challengesetlist.MyChallengeSetsVm

class DownloadChallengeList : ChallengeSetList() {
    override val vm: ChallengeSetListVm by viewModels<DownloadChallengeSetsVm>()

    override fun initButton(){
        val addButton = findViewById<Button>(R.id.button_add_challenge_set)
        addButton.visibility = View.GONE
    }

    override fun getRecyclerViewAdapter(): ChallengeSetsAdapter {
        return DownloadChallengeSetsAdapter(this, vm.challengeSetOverviews)
    }
}