package edu.bth.ma.passthebomb.client.viewmodel.challengesetlist

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview
import edu.bth.ma.passthebomb.client.viewmodel.DatabaseVm

abstract class ChallengeSetListVm(application: Application): DatabaseVm(application) {
    lateinit var challengeSetOverviews: ArrayList<ChallengeSetOverview>
    open fun init(challengeSets: List<ChallengeSetOverview>){
        challengeSetOverviews = ArrayList(challengeSets)
    }
    abstract fun onChallengeSetClick(index: Int, context: Context)
    abstract fun onButton(activity: AppCompatActivity)
}