package edu.bth.ma.passthebomb.client.data

import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview

class Rest {

    //TODO
    fun loadOnlineChallengeSetOverviews(): ArrayList<ChallengeSetOverview>{
        return arrayListOf<ChallengeSetOverview>(
            ChallengeSetOverview("0", "online test1", 0),
            ChallengeSetOverview("1", "online test2", 42),
            ChallengeSetOverview("2", "online test3", 3)
        )
    }
}