package edu.bth.ma.passthebomb.client.view.challengesetlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview
import edu.bth.ma.passthebomb.client.view.GameSettingsActivity
import edu.bth.ma.passthebomb.client.viewmodel.challengesetlist.SelectChallengeSetsVm

class SelectChallengeSetsActivity : ChallengeSetListActivity() {

    override val vm: SelectChallengeSetsVm by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Select Challenge Sets"
    }


    override fun initButton(){
        val startButton = findViewById<MaterialButton>(R.id.button_add_challenge_set)
        startButton.setIconResource(R.drawable.ic_arrow_right)
        startButton.text = "Start Game!"
        startButton.visibility = View.VISIBLE
    }

    override fun onButtonClick(view: View) {
        if((vm.selectedChallengeSetIndices.value?.size ?: 0) == 0){
            Toast.makeText(
                this,
                "Please select at least one challenge set.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if(vm.challengeSetOverviews.value!=null){
            val challengeSetIds = ArrayList<String>()
            val set: HashSet<Int> = vm.selectedChallengeSetIndices.value ?: HashSet<Int>()
            for(index in set){
                challengeSetIds.add(vm.challengeSetOverviews.value!![index].id)
            }
            val intent = Intent(this, GameSettingsActivity::class.java)
            intent.putExtra("CHALLENGE_SET_IDS", challengeSetIds)
            startActivity(intent)
        }
    }

    override fun onChallengeSetClick(
        view: View,
        challengeSets: ArrayList<ChallengeSetOverview>,
        position: Int
    ) {
        vm.selectChallengeSet(position)
    }

    override fun getChallengeSetsAdapter(challengeSets: ArrayList<ChallengeSetOverview>): ChallengeSetsAdapter {
        return SelectChallengeSetsAdapter(this, challengeSets)
    }

    inner class SelectChallengeSetsAdapter(
        context: Context,
        dataset: ArrayList<ChallengeSetOverview>
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
                onChallengeSetClick(holder.view, dataset, position)
            }
            vm.selectedChallengeSetIndices.observe(this@SelectChallengeSetsActivity,
                Observer{set ->
                checkBox.isChecked = set.contains(position)
            })
        }
    }
}