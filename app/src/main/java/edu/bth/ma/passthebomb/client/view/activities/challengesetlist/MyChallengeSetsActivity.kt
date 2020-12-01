package edu.bth.ma.passthebomb.client.view.activities.challengesetlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.viewModels
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview

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

    class MyChallengeSetsAdapter(private val context: Context,
                                 private val dataset: List<ChallengeSetOverview>
    ) : ChallengeSetsAdapter(context, dataset){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val adapterLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_my_challenge_set, parent, false)
            return ItemViewHolder(adapterLayout)
        }
    }
}