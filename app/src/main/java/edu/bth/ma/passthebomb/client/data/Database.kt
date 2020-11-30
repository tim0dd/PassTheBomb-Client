package edu.bth.ma.passthebomb.client.data

import edu.bth.ma.passthebomb.client.model.Challenge
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview


class Database{
    //TODO
    fun loadLocalChallengeSetOverviews(): ArrayList<ChallengeSetOverview>{
        return arrayListOf<ChallengeSetOverview>(
            ChallengeSetOverview("0", "test1", 0),
            ChallengeSetOverview("1", "test2", 42),
            ChallengeSetOverview("2", "test3", 3)
        )
    }

}