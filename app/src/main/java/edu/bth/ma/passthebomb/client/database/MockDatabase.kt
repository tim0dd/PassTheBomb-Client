package edu.bth.ma.passthebomb.client.database

import edu.bth.ma.passthebomb.client.model.Challenge
import edu.bth.ma.passthebomb.client.model.ChallengeSet
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview

/**
 * Only created for testing purposes, later we will be using ChallengeSetDatabase
 */
class MockDatabase {

    //TODO
    fun loadLocalChallengeSetOverviews(): ArrayList<ChallengeSetOverview>{
        return arrayListOf<ChallengeSetOverview>(
            ChallengeSetOverview("0", "test1", 0),
            ChallengeSetOverview("1", "test2", 42),
            ChallengeSetOverview("2", "test3", 3)
        )
    }

    //TODO
    fun loadChallengeSet(challengeSetId: String): ChallengeSet{
        val challengeSet = ChallengeSet(challengeSetId, "AnimalChallengeSet", 42)
        challengeSet.challenges.add(Challenge("Name it", "AnimalChallengeSet", 20))
        challengeSet.challenges.add(Challenge("Do it", "AnimalChallengeSet",5))
        challengeSet.challenges.add(Challenge("Move it", "AnimalChallengeSet", 15))
        return challengeSet
    }

    //TODO
    fun loadChallenge(challengeSetId: String, challengeId: Int): Challenge{
        return Challenge("ChallengeId", "ChallengeSetId",  20)
    }

    fun deleteChallenge(challenge: Challenge){
        //TODO
    }

    fun storeChallenge(challenge: Challenge?){
        if(challenge==null){
            return
        }
        //TODO
    }



}