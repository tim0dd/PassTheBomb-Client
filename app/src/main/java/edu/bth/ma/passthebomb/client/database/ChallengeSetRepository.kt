package edu.bth.ma.passthebomb.client.database

import androidx.lifecycle.LiveData
import edu.bth.ma.passthebomb.client.model.ChallengeSet
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview

class ChallengeSetRepository(private val challengeSetDao: ChallengeSetDao) {

    fun getAllChallengeSetOverviews(): LiveData<List<ChallengeSetOverview>> {
        return challengeSetDao.getAllChallengeSetOverviews()
    }

    fun getChallengeSetOverview(id: Int): LiveData<ChallengeSetOverview> {
        return challengeSetDao.getChallengeSetOverview(id)
    }

    fun getAllChallengeSets(id: Int): LiveData<List<ChallengeSet>> {
        return challengeSetDao.getAllChallengeSets()
    }

    fun getChallengeSet(id: Int): LiveData<ChallengeSet?> {
        return challengeSetDao.getChallengeSet(id)
    }

    fun addChallengeSetOverview(challengeSetOverview: ChallengeSetOverview) {
        challengeSetDao.addChallengeSetOverview(challengeSetOverview)
    }

    fun updateChallengeSetOverview(challengeSetOverview: ChallengeSetOverview) {
        challengeSetDao.updateChallengeSetOverview(challengeSetOverview)
    }

    fun deleteChallengeSetOverview(challengeSetOverview: ChallengeSetOverview) {
        challengeSetDao.deleteChallengeSetOverview(challengeSetOverview)
    }

    fun deleteChallengeSetOverview(id: Int) {
        challengeSetDao.deleteChallengeSetOverview(id)
    }

    fun deleteAllChallengeSetOverviews() {
        challengeSetDao.deleteAllChallengeSetOverviews()
    }
}