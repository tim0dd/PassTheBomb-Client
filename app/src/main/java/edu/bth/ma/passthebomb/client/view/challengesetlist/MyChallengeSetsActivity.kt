package edu.bth.ma.passthebomb.client.view.challengesetlist

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.model.ChallengeSet
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview
import edu.bth.ma.passthebomb.client.view.ChallengeSetActivity
import edu.bth.ma.passthebomb.client.view.Dialogs
import edu.bth.ma.passthebomb.client.viewmodel.DatabaseVm
import edu.bth.ma.passthebomb.client.viewmodel.challengesetlist.ChallengeSetListVm
import edu.bth.ma.passthebomb.client.viewmodel.challengesetlist.MyChallengeSetsVm

class MyChallengeSetsActivity : ChallengeSetListActivity() {

    override val vm by viewModels<MyChallengeSetsVm>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "My Challenge Sets"
    }

    override fun initButton(){
        val addButton = findViewById<Button>(R.id.button_add_challenge_set)
        addButton.visibility = View.VISIBLE
    }

    override fun onButtonClick(view: View) {
        val dia = Dialogs(this)
        dia.showStringInputDialog("Challenge Set Name"){
            if(it != ""){
                val newChallengeSet = ChallengeSet.generateNewFromContext(this, it)
                vm.addChallengeSet(newChallengeSet)
                val intent = Intent(this, ChallengeSetActivity::class.java)
                intent.putExtra("CHALLENGE_SET_ID", newChallengeSet.challengeSetOverview.id)
                startActivity(intent)
            }
        }
    }

    override fun onChallengeSetClick(
        view: View,
        challengeSets: ArrayList<ChallengeSetOverview>,
        position: Int
    ) {
        val intent = Intent(getApplication(), ChallengeSetActivity::class.java)
        intent.putExtra("CHALLENGE_SET_ID", challengeSets[position].id)
        startActivity(intent)
    }

    override fun getChallengeSetsAdapter(challengeSetOverviews: ArrayList<ChallengeSetOverview>): ChallengeSetsAdapter {
        return MyChallengeSetsAdapter(this, challengeSetOverviews)
    }

    inner class MyChallengeSetsAdapter(private val context: Context,
                                       dataset: ArrayList<ChallengeSetOverview>
    ) : ChallengeSetsAdapter(context, dataset){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val adapterLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_my_challenge_set, parent, false)
            return ItemViewHolder(adapterLayout)
        }
    }


}