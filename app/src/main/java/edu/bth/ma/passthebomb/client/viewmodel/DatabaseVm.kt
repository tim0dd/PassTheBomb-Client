package edu.bth.ma.passthebomb.client.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import edu.bth.ma.passthebomb.client.database.ChallengeRepository
import edu.bth.ma.passthebomb.client.database.AppDb
import edu.bth.ma.passthebomb.client.database.ChallengeSetRepository
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview
import edu.bth.ma.passthebomb.client.model.Challenge
import edu.bth.ma.passthebomb.client.model.ChallengeSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


open class DatabaseVm(application: Application) : AndroidViewModel(application), EventCreator {

    override val events = MutableLiveData<(AppCompatActivity) -> Unit>()

    private val challengeSetRepo: ChallengeSetRepository =
        AppDb.getDatabase(application).getChallengeSetRepository()
    private val challengeRepo: ChallengeRepository =
        AppDb.getDatabase(application).getChallengeRepository()


    fun scheduleEvent(event: (AppCompatActivity) -> Unit){
        events.value = event
    }

    fun shortUserMessage(message: String){
        scheduleEvent { context ->
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun getAllChallenges(): LiveData<List<Challenge>> {
        return challengeRepo.getAllChallenges()
    }

    fun getChallenge(id: String): LiveData<Challenge?> {
        return challengeRepo.getChallenge(id)
    }

    fun getChallengesByOverviewId(overviewId: String): LiveData<List<Challenge>> {
        return challengeRepo.getChallengesByOverviewId(overviewId)
    }

    fun getChallengesByOverviewIds(idList: List<String>): LiveData<List<Challenge>> {
        return challengeRepo.getChallengesByOverviewIds(idList)
    }

    fun getAllChallengeSets(): LiveData<List<ChallengeSetOverview>> {
        return challengeSetRepo.getAllChallengeSetOverviews()
    }

    fun getChallengeSetOverview(id: String): LiveData<ChallengeSetOverview> {
        return challengeSetRepo.getChallengeSetOverview(id)
    }

    fun getChallengeSet(id: String): LiveData<ChallengeSet?> {
        return challengeSetRepo.getChallengeSet(id)
    }

    fun addChallengeSetOverview(challengeSetOverview: ChallengeSetOverview) {
        runCoroutine { challengeSetRepo.addChallengeSetOverview(challengeSetOverview) }
    }

    fun addChallengeSet(challengeSet: ChallengeSet) {
        runCoroutine { challengeSetRepo.addChallengeSetOverview(challengeSet.challengeSetOverview) }
        challengeSet.challenges.forEach { runCoroutine { challengeRepo.addChallenge(it) } }
    }

    fun addChallenge(challenge: Challenge) {
        runCoroutine { challengeRepo.addChallenge(challenge) }
    }

    fun updateChallengeSet(challengeSetOverview: ChallengeSetOverview) {
        runCoroutine { challengeSetRepo.updateChallengeSetOverview(challengeSetOverview) }
    }

    fun updateChallenge(challenge: Challenge){
        runCoroutine { challengeRepo.updateChallenge(challenge) }
    }

    fun deleteChallenge(challengeId: String){
        runCoroutine { challengeRepo.deleteChallenge(challengeId) }
    }

    fun updateChallengeSet(challengeSet: ChallengeSet) {
        runCoroutine { challengeSetRepo.updateChallengeSetOverview(challengeSet.challengeSetOverview) }
        challengeSet.challenges.forEach { runCoroutine { challengeRepo.updateChallenge(it) } }
    }

    fun deleteChallengeSet(challengeSet: ChallengeSet) {
        runCoroutine { challengeSetRepo.deleteChallengeSetOverview(challengeSet.challengeSetOverview) }
        challengeSet.challenges.forEach { runCoroutine { challengeRepo.deleteChallenge(it) } }
    }

    fun deleteChallengeSet(challengeSetOverview: ChallengeSetOverview) {
        runCoroutine { challengeSetRepo.deleteChallengeSetOverview(challengeSetOverview) }
    }


    fun deleteChallengeSet(id: String) {
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
