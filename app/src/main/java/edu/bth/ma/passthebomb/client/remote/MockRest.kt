package edu.bth.ma.passthebomb.client.remote

import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview

class MockRest {

    //TODO
    fun loadOnlineChallengeSetOverviews(): ArrayList<ChallengeSetOverview>{
        return arrayListOf<ChallengeSetOverview>(
            ChallengeSetOverview("0", "online test1", 0),
            ChallengeSetOverview("1", "online test2", 42),
            ChallengeSetOverview("2", "online test3", 3)
        )
    }


}