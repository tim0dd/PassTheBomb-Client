package edu.bth.ma.passthebomb.client.viewmodel

import android.app.Application
import androidx.annotation.Nullable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import edu.bth.ma.passthebomb.client.database.ChallengeSetDatabase
import edu.bth.ma.passthebomb.client.database.ChallengeSetEntity
import edu.bth.ma.passthebomb.client.database.ChallengeSetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DatabaseVm(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<ChallengeSetEntity>>
    private val repository: ChallengeSetRepository
    private var currentChallengeSetList = listOf<ChallengeSetEntity>()

    init {
        val challengeSetDao = ChallengeSetDatabase.getDatabase(
            application
        ).challengeSetDao()
        repository = ChallengeSetRepository(challengeSetDao)
        readAllData = repository.readAllData
        readAllData.observeForever {
            if (it != null) currentChallengeSetList = it
        }
    }


    fun getChallengeSet(id: Int): ChallengeSetEntity? {
        return currentChallengeSetList.filter { c -> c.id == id }.getOrNull(0)
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