package edu.bth.ma.passthebomb.client.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import edu.bth.ma.passthebomb.client.R

import edu.bth.ma.passthebomb.client.data.Database
import edu.bth.ma.passthebomb.client.view.adapter.ChallengeSetAdapter

class MyChallengeSetsScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ativity_my_challenge_sets)

        val myChallengeSetOverviewList = Database().loadLocalChallengeSetOverviews()
        val recyclerView = findViewById<RecyclerView>(R.id.recycleviewMyChallengeSets)
        recyclerView.adapter = ChallengeSetAdapter(this, myChallengeSetOverviewList)
        recyclerView.setHasFixedSize(true)
    }


}