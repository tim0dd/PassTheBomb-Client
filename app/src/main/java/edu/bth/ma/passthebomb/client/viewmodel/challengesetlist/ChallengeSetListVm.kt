package edu.bth.ma.passthebomb.client.viewmodel.challengesetlist

import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview

interface ChallengeSetListVm {
    fun onChallengeSetClick(index: Int)
    fun onButton()
    val challengeSetOverviews: ArrayList<ChallengeSetOverview>
}