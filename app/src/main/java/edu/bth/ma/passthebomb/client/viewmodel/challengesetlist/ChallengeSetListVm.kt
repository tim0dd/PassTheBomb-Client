package edu.bth.ma.passthebomb.client.viewmodel.challengesetlist

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview
import edu.bth.ma.passthebomb.client.viewmodel.DatabaseVm

abstract class ChallengeSetListVm(application: Application): DatabaseVm(application) {
    val challengeSetOverviews = MutableLiveData <ArrayList<ChallengeSetOverview>>()

    abstract fun init(context: AppCompatActivity)

    abstract fun onChallengeSetClick(index: Int, context: Context)
    abstract fun onButton(activity: AppCompatActivity)
}