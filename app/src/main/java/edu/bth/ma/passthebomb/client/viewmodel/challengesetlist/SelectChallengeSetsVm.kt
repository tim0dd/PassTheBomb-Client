package edu.bth.ma.passthebomb.client.viewmodel.challengesetlist

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import edu.bth.ma.passthebomb.client.data.Database
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview
import edu.bth.ma.passthebomb.client.view.activities.GameSettingsActivity
import edu.bth.ma.passthebomb.client.view.activities.TutorialActivity

class SelectChallengeSetsVm() : ViewModel(), ChallengeSetListVm {
    override val challengeSetOverviews: ArrayList<ChallengeSetOverview> = Database().loadLocalChallengeSetOverviews()
    override fun onChallengeSetClick(index: Int, context: Context) {
        TODO("Not yet implemented")
    }

    override fun onButton(activity: AppCompatActivity) {
        val intent = Intent(activity, GameSettingsActivity::class.java).apply {  }
        activity.startActivity(intent)
    }
}
