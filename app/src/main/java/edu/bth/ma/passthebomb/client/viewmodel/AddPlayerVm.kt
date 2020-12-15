package edu.bth.ma.passthebomb.client.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.bth.ma.passthebomb.client.model.Challenge
import edu.bth.ma.passthebomb.client.model.GameSettings
import edu.bth.ma.passthebomb.client.preferences.PreferenceService
import edu.bth.ma.passthebomb.client.utils.ObserveExtensions.Companion.observeOnce

class AddPlayerVm(application: Application) : DatabaseVm(application) {
    var playerNames = MutableLiveData<ArrayList<String>>()
    lateinit var gameSettings: GameSettings
    var challenges: ArrayList<Challenge> = arrayListOf()
    lateinit var challengesLiveData: LiveData<List<Challenge>>

    fun init(gameSettings: GameSettings?) {
        this.gameSettings = gameSettings ?: GameSettings(ArrayList(), ArrayList(), 1.0, 1.0)
        challengesLiveData = getChallengesByOverviewIds(gameSettings!!.challengeSetIds)
        challengesLiveData.observeOnce {
            challenges.addAll(it)
        }
        val namesArray = arrayListOf<String>()
        namesArray.addAll(PreferenceService.getInstance(getApplication()).getPlayerNames())
        playerNames.value = namesArray
    }

    fun addPlayer(playerName: String) {
        var list = playerNames.value
        if (list == null) {
            list = ArrayList()
        }
        list.add(playerName)
        playerNames.value = list
        if (list != null) PreferenceService.getInstance(getApplication())
            .setPlayerNames(list.toSet())
    }

    fun deletePlayer(index: Int) {
        val list = playerNames.value
        list?.removeAt(index)
        playerNames.value = list
        if (list != null) PreferenceService.getInstance(getApplication())
            .setPlayerNames(list.toSet())
    }

    fun checkConfiguration(): Boolean{
        if ((playerNames.value?.size ?: 0) < 2) {
            shortUserMessage("Please add at least two players.")
            return false
        }
        //this should never happen since the database should be way faster then the user, but just to make sure...
        if(challengesLiveData.hasActiveObservers()){
            shortUserMessage("We are still loading your challenges in background, please wait a bit before starting the game.")
        }
        if (challenges.isEmpty()) {
            shortUserMessage("No challenges found in selected challenge sets")
            return false
        }
        return true
    }

    fun getGameSettingsWithPlayer(): GameSettings{
        gameSettings.playerList = playerNames.value ?: ArrayList()
        return gameSettings
    }
}

