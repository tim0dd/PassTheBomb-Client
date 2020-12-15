package edu.bth.ma.passthebomb.client.viewmodel.challengesetlist

import android.app.Application
import androidx.lifecycle.Observer
import edu.bth.ma.passthebomb.client.utils.ObserveExtensions.Companion.observeOnce
import kotlin.collections.ArrayList

class MyChallengeSetsVm(application: Application) : ChallengeSetListVm(application) {

    init{
        getAllChallengeSets().observeOnce {
            challengeSetOverviews.value = ArrayList(it)
        }
    }
}
