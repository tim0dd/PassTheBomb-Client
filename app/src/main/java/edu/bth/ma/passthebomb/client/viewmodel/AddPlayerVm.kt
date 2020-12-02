package edu.bth.ma.passthebomb.client.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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

