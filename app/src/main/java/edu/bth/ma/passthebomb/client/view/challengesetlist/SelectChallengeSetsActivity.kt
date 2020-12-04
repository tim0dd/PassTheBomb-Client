package edu.bth.ma.passthebomb.client.view.challengesetlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview
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

    inner class SelectChallengeSetsAdapter(private val context: Context,
                                     private val dataset: List<ChallengeSetOverview>
    ) : ChallengeSetsAdapter(context, dataset){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val adapterLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_selectable_challenge_set, parent, false)
            return ItemViewHolder(adapterLayout)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val challengeSetIncludedObserver = Observer<Set<Int>>{set ->
                val checkBox: CheckBox = holder.view.findViewById(R.id.check_box_challenge_set_selected_for_game)
                checkBox.isSelected = set.contains(position)
            }

        }
    }
}