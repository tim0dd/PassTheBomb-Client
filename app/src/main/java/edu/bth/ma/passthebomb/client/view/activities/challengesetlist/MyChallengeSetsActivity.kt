package edu.bth.ma.passthebomb.client.view.activities.challengesetlist

import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.view.adapter.ChallengeSetsAdapter

import edu.bth.ma.passthebomb.client.view.adapter.MyChallengeSetsAdapter
import edu.bth.ma.passthebomb.client.viewmodel.challengesetlist.ChallengeSetListVm
import edu.bth.ma.passthebomb.client.viewmodel.challengesetlist.MyChallengeSetsVm

class MyChallengeSetsActivity : ChallengeSetListActivity() {

    override val vm: ChallengeSetListVm by viewModels<MyChallengeSetsVm>()

    override fun initButton(){
        val addButton = findViewById<Button>(R.id.button_add_challenge_set)
        addButton.visibility = View.VISIBLE
    }

    override fun getRecyclerViewAdapter(): ChallengeSetsAdapter {
        return MyChallengeSetsAdapter(this, vm.challengeSetOverviews)
    }
}