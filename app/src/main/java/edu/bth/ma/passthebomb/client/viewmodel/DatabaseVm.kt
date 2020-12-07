package edu.bth.ma.passthebomb.client.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import edu.bth.ma.passthebomb.client.database.ChallengeRepository
import edu.bth.ma.passthebomb.client.database.AppDb
import edu.bth.ma.passthebomb.client.database.ChallengeSetRepository
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview
import edu.bth.ma.passthebomb.client.model.Challenge
import edu.bth.ma.passthebomb.client.model.ChallengeSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


open class DatabaseVm(application: Application) : AndroidViewModel(application) {

    private val challengeSetRepo: ChallengeSetRepository
    private val challengeRepo: ChallengeRepository

    init {
        val challengeDao = AppDb.getDatabase(application).challengeDao()
        val challengeSetDao = AppDb.getDatabase(application).challengeSetDao()
        challengeSetRepo = ChallengeSetRepository(challengeSetDao)
        challengeRepo = ChallengeRepository(challengeDao)
    }

    fun getAllChallenges(): LiveData<List<Challenge>> {
        return challengeRepo.getAllChallenges()
    }

    fun getChallenge(id: Int): LiveData<Challenge?> {
        return challengeRepo.getChallenge(id)
    }

    fun getChallenges(challengeSetId: Int): LiveData<List<Challenge>> {
        return challengeRepo.getAllChallenges(challengeSetId)
    }

    fun getChallenges(idList: List<Int>): LiveData<List<Challenge>> {
        return challengeRepo.getChallenges(idList)
    }

    fun getAllChallengeSets(): LiveData<List<ChallengeSetOverview>> {
        return challengeSetRepo.getAllChallengeSetOverviews()
    }

    fun getChallengeSetOverview(id: Int): LiveData<ChallengeSetOverview> {
        return challengeSetRepo.getChallengeSetOverview(id)
    }

    fun getChallengeSet(id: Int): LiveData<ChallengeSet?> {
        return challengeSetRepo.getChallengeSet(id)
    }

    fun addChallengeSet(challengeSetOverview: ChallengeSetOverview) {
        runCoroutine { challengeSetRepo.addChallengeSetOverview(challengeSetOverview) }
    }

    fun addChallenge(challenge: Challenge) {
        runCoroutine { challengeRepo.addChallenge(challenge) }
    }

    fun updateChallengeSet(challengeSetOverview: ChallengeSetOverview) {
        runCoroutine { challengeSetRepo.updateChallengeSetOverview(challengeSetOverview) }
    }

    fun updateChallengeSet(challengeSet: ChallengeSet) {
        runCoroutine { challengeSetRepo.updateChallengeSetOverview(challengeSet.challengeSetOverview) }
        challengeSet.challenges.forEach { runCoroutine { challengeRepo.updateChallenge(it) } }
    }

    fun deleteChallengeSet(challengeSet: ChallengeSet) {
        runCoroutine { challengeSetRepo.deleteChallengeSetOverview(challengeSet.challengeSetOverview) }
    }

    fun deleteChallengeSet(challengeSetOverview: ChallengeSetOverview) {
        runCoroutine { challengeSetRepo.deleteChallengeSetOverview(challengeSetOverview) }
    }


    fun deleteChallengeSet(id: Int) {
        runCoroutine { challengeSetRepo.deleteChallengeSetOverview(id) }
    }

    fun deleteAllChallengeSets() {
        runCoroutine { challengeSetRepo.deleteAllChallengeSetOverviews() }
    }

    private fun runCoroutine(function: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            function()
        }
    }
}
