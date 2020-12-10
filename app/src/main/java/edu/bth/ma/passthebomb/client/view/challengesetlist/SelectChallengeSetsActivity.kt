package edu.bth.ma.passthebomb.client.view.challengesetlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview
import edu.bth.ma.passthebomb.client.viewmodel.challengesetlist.SelectChallengeSetsVm

class SelectChallengeSetsActivity : ChallengeSetListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Select Challenge Sets"
    }

    override val vm: SelectChallengeSetsVm by viewModels()

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
            val checkBox: CheckBox = holder.view.findViewById(R.id.check_box_challenge_set_selected_for_game)
            checkBox.setOnClickListener{
                vm.onChallengeSetClick(position, this@SelectChallengeSetsActivity)
            }
            val challengeSetIncludedObserver = Observer<Set<Int>>{set ->
                checkBox.isChecked = set.contains(position)
            }
            vm.selectedChallengeSetIndices.observe(this@SelectChallengeSetsActivity, challengeSetIncludedObserver)
        }
    }
}