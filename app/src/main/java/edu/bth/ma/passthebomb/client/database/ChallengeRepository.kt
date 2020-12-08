package edu.bth.ma.passthebomb.client.database

import androidx.lifecycle.LiveData
import edu.bth.ma.passthebomb.client.model.Challenge

class ChallengeRepository(private val challengeDao: ChallengeDao) {

    fun getAllChallenges(): LiveData<List<Challenge>> {
        return challengeDao.getAllChallenges()
    }

    fun getChallengesByOverviewId(challengeSetId: Int): LiveData<List<Challenge>> {
        return challengeDao.getChallengesByOverviewId(challengeSetId)
    }

    fun getChallengesByOverviewIds(idList: List<Int>): LiveData<List<Challenge>> {
        return challengeDao.getChallengesByOverviewIds(idList)
    }

    fun getChallenge(id: Int): LiveData<Challenge?> {
        return challengeDao.getChallenge(id)
    }

    fun addChallenge(challenge: Challenge) {
        challengeDao.addChallenge(challenge)
    }

    fun updateChallenge(challenge: Challenge) {
        challengeDao.updateChallenge(challenge)
    }

    fun deleteChallenge(challenge: Challenge) {
        challengeDao.deleteChallenge(challenge)
    }

    fun deleteChallenge(id: Int) {
        challengeDao.deleteChallenge(id)
    }

    fun deleteAllChallenges() {
        challengeDao.deleteAllChallenges()
    }
}