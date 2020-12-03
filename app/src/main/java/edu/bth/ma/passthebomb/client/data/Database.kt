package edu.bth.ma.passthebomb.client.data

import edu.bth.ma.passthebomb.client.model.Challenge
import edu.bth.ma.passthebomb.client.model.ChallengeSet
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

    //TODO
    fun loadChallengeSet(challengeSetId: String): ChallengeSet{
        val challengeSet = ChallengeSet(challengeSetId, "AnimalChallengeSet", 42)
        challengeSet.challenges.add(Challenge("Name it", "AnimalChallengeSet","Name an animal with A!", 20))
        challengeSet.challenges.add(Challenge("Do it", "AnimalChallengeSet","Bark like a dog 3 times", 5))
        challengeSet.challenges.add(Challenge("Move it", "AnimalChallengeSet","Lay phone down, make 3 frog hops.", 15))
        return challengeSet
    }

    //TODO
    fun loadChallenge(challengeSetId: String, challengeId: String): Challenge{
        return Challenge("ChallengeId", "ChallengeSetId", "Name an animal with A!", 20)
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