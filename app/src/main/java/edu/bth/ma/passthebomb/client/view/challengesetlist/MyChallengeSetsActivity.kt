package edu.bth.ma.passthebomb.client.view.challengesetlist

import android.content.Context
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
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview
import edu.bth.ma.passthebomb.client.viewmodel.DatabaseVm
import edu.bth.ma.passthebomb.client.viewmodel.challengesetlist.ChallengeSetListVm
import edu.bth.ma.passthebomb.client.viewmodel.challengesetlist.MyChallengeSetsVm

class MyChallengeSetsActivity : ChallengeSetListActivity() {

    override val vm: ChallengeSetListVm by viewModels<MyChallengeSetsVm>()
    private val databaseVm by viewModels<DatabaseVm>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "My Challenge Sets"

        databaseVm.getAllChallengeSets().observe(this,
        Observer {
            val myChallengeSetsAdapter = MyChallengeSetsAdapter(this@MyChallengeSetsActivity, it)
            this@MyChallengeSetsActivity.challengeSetsAdapter = myChallengeSetsAdapter
            vm.init(it)
        })
    }

    override fun initButton(){
        val addButton = findViewById<Button>(R.id.button_add_challenge_set)
        addButton.visibility = View.VISIBLE
    }

    inner class MyChallengeSetsAdapter(private val context: Context,
                                 private val dataset: List<ChallengeSetOverview>
    ) : ChallengeSetsAdapter(context, dataset){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val adapterLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_my_challenge_set, parent, false)
            return ItemViewHolder(adapterLayout)
        }
    }


}