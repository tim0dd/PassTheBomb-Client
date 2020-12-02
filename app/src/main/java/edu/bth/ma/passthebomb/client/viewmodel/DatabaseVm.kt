package edu.bth.ma.passthebomb.client.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import edu.bth.ma.passthebomb.client.database.ChallengeSetDatabase
import edu.bth.ma.passthebomb.client.database.ChallengeSetEntity
import edu.bth.ma.passthebomb.client.database.ChallengeSetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatabaseVm(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<ChallengeSetEntity>>
    private val repository: ChallengeSetRepository

    init {
        val challengeSetDao = ChallengeSetDatabase.getDatabase(
            application
        ).challengeSetDao()
        repository = ChallengeSetRepository(challengeSetDao)
        readAllData = repository.readAllData
    }

    fun getChallengeSet(id: Int) : ChallengeSetEntity? {
       return readAllData.value?.filter { c -> c.id == id }?.get(0)
     /*   viewModelScope.launch(Dispatchers.IO) {
            repository.getChallengeSet(id)

        }*/
    }

    fun addChallengeSet(ChallengeSet: ChallengeSetEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addChallengeSet(ChallengeSet)
        }
    }

    fun updateChallengeSet(ChallengeSet: ChallengeSetEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateChallengeSet(ChallengeSet)
        }
    }

    fun deleteChallengeSet(ChallengeSet: ChallengeSetEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteChallengeSet(ChallengeSet)
        }
    }

    fun deleteAllChallengeSets() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllChallengeSets()
        }
    }

}