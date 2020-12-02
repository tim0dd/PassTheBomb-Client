package edu.bth.ma.passthebomb.client.database

import androidx.lifecycle.LiveData

class ChallengeSetRepository(private val challengeSetDao: ChallengeSetDao) {

    val readAllData: LiveData<List<ChallengeSetEntity>> = challengeSetDao.getChallenges()

    suspend fun addChallengeSet(challengeSetEntity: ChallengeSetEntity) {
        challengeSetDao.addChallengeSet(challengeSetEntity)
    }

}