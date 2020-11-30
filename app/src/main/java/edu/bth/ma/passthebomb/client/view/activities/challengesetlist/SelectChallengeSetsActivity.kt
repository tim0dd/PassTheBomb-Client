package edu.bth.ma.passthebomb.client.view.activities.challengesetlist

import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.view.adapter.ChallengeSetsAdapter

import edu.bth.ma.passthebomb.client.view.adapter.SelectChallengeSetsAdapter
import edu.bth.ma.passthebomb.client.viewmodel.challengesetlist.ChallengeSetListVm
import edu.bth.ma.passthebomb.client.viewmodel.challengesetlist.SelectChallengeSetsVm

class SelectChallengeSetsActivity : ChallengeSetListActivity() {
    override val vm: ChallengeSetListVm by viewModels<SelectChallengeSetsVm>()

    override fun initButton(){
        val startButton = findViewById<Button>(R.id.button_add_challenge_set)
        startButton.text = "Start Game!"
        startButton.visibility = View.VISIBLE
    }

    override fun getRecyclerViewAdapter(): ChallengeSetsAdapter {
        return SelectChallengeSetsAdapter(this, vm.challengeSetOverviews)
    }
}