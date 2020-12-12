package edu.bth.ma.passthebomb.client.database

import edu.bth.ma.passthebomb.client.model.Challenge
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview
import edu.bth.ma.passthebomb.client.model.ChallengeSet
import edu.bth.ma.passthebomb.client.utils.IdGenerator
import java.util.*
import kotlin.collections.ArrayList

/**
 * Only created for testing purposes, later we will be using ChallengeSetDatabase
 */
class MockDatabase {

    //TODO
    fun loadLocalChallengeSets(): ArrayList<ChallengeSetOverview>{
        val now = Date(System.currentTimeMillis())

        val challenge1 = Challenge(IdGenerator().generateDbId(), "0", now, "First challenge text", 100)
        val challenge2 = Challenge(IdGenerator().generateDbId(), "0", now, "First challenge text", 100)
        val challengeList = listOf(challenge1, challenge2)
        val challengeSet = ChallengeSetOverview("0", "0", "Animals", now, now, now, 1337)
        return arrayListOf<ChallengeSetOverview>(
            challengeSet
        )
    }

    //TODO
    fun loadChallengeSet(challengeSetId: String): ChallengeSet{
        val now = Date(System.currentTimeMillis())
        val challenge1 = Challenge(IdGenerator().generateDbId(), "0", now, "First challenge text", 100)
        val challenge2 = Challenge(IdGenerator().generateDbId(), "0", now, "First challenge text", 100)
        val challengeList = listOf(challenge1, challenge2)
        val challengeSet = ChallengeSetOverview("0", "0", "Animals", now, now, now, 1337)
        return ChallengeSet(challengeSet,challengeList)
    }

    //TODO
    fun loadChallenge(challengeId: Int): Challenge{
        val now = Date(System.currentTimeMillis())
        return Challenge(IdGenerator().generateDbId(), "0", now, "First challenge text", 100)
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