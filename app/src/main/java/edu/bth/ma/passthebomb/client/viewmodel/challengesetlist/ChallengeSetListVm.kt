package edu.bth.ma.passthebomb.client.viewmodel.challengesetlist

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview

interface ChallengeSetListVm {
    fun onChallengeSetClick(index: Int)
    fun onButton(activity: AppCompatActivity)
    val challengeSetOverviews: ArrayList<ChallengeSetOverview>
}