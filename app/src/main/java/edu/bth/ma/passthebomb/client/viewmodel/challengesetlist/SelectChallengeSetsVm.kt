package edu.bth.ma.passthebomb.client.viewmodel.challengesetlist

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import edu.bth.ma.passthebomb.client.database.MockDatabase
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview
import edu.bth.ma.passthebomb.client.view.GameSettingsActivity

class SelectChallengeSetsVm() : ViewModel(), ChallengeSetListVm {
    override val challengeSetOverviews: ArrayList<ChallengeSetOverview> = MockDatabase().loadLocalChallengeSetOverviews()
    override fun onChallengeSetClick(index: Int, context: Context) {
        TODO("Not yet implemented")
    }

    override fun onButton(activity: AppCompatActivity) {
        val intent = Intent(activity, GameSettingsActivity::class.java).apply {  }
        activity.startActivity(intent)
    }
}
