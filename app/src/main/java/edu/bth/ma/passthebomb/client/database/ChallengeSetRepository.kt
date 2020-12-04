package edu.bth.ma.passthebomb.client.database

import androidx.lifecycle.LiveData

class ChallengeSetRepository(private val challengeSetDao: ChallengeSetDao) {

    val readAllData: LiveData<List<ChallengeSetEntity>> = challengeSetDao.getChallengeSets()

    fun getChallengeSet(id: Int) : LiveData<ChallengeSetEntity>  {
        return challengeSetDao.getChallengeSet(id)
    }

    fun addChallengeSet(challengeSetEntity: ChallengeSetEntity) {
        challengeSetDao.addChallengeSet(challengeSetEntity)
    }

    fun updateChallengeSet(challengeSetEntity: ChallengeSetEntity) {
        challengeSetDao.updateChallengeSet(challengeSetEntity)
    }

    fun deleteChallengeSet(challengeSetEntity: ChallengeSetEntity) {
        challengeSetDao.deleteChallengeSet(challengeSetEntity)
    }

    fun deleteChallengeSet(id: Int) {
        challengeSetDao.deleteChallengeSet(id)
    }

    fun deleteAllChallengeSets() {
        challengeSetDao.deleteAllChallengeSets()
    }
}