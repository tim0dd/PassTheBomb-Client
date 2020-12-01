package edu.bth.ma.passthebomb.client.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import edu.bth.ma.passthebomb.client.data.Database
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview
import androidx.lifecycle.MutableLiveData

class AddPlayerVm() : ViewModel() {
    var playerNames = MutableLiveData<ArrayList<String>>(arrayListOf("karl", "Bernd", "Harald"))

    fun addPlayer(playerName: String){
        val list = playerNames.value
        list?.add(playerName)
        playerNames.value = list
    }

    fun deletePlayer(index: Int){
        val list = playerNames.value
        list?.removeAt(index)
        playerNames.value = list
    }
}

