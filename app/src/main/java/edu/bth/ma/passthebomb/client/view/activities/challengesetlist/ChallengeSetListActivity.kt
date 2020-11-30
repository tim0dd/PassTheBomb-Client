package edu.bth.ma.passthebomb.client.view.activities.challengesetlist

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.view.adapter.ChallengeSetsAdapter

import edu.bth.ma.passthebomb.client.viewmodel.challengesetlist.ChallengeSetListVm

abstract class ChallengeSetListActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    abstract val vm: ChallengeSetListVm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_my_challenge_sets)

        recyclerView = findViewById<RecyclerView>(R.id.recycleviewMyChallengeSets)
        recyclerView.adapter = getRecyclerViewAdapter()
        recyclerView.setHasFixedSize(true)

        initButton()

        val button : Button = findViewById<Button>(R.id.button_add_challenge_set)
        button.setOnClickListener{vm.onButton(this)}
    }

    abstract fun initButton()

    abstract fun getRecyclerViewAdapter(): ChallengeSetsAdapter
}