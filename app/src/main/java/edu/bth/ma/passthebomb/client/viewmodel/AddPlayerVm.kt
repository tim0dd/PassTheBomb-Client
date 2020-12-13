package edu.bth.ma.passthebomb.client.viewmodel

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bth.ma.passthebomb.client.model.GameSettings
import edu.bth.ma.passthebomb.client.view.GameActivity

class AddPlayerVm() : ViewModel() {
    var playerNames = MutableLiveData<ArrayList<String>>()
    lateinit var gameSettings: GameSettings

    fun init(gameSettings: GameSettings?){
        this.gameSettings = gameSettings ?: GameSettings(ArrayList<String>(), ArrayList<String>(), 1.0, 1.0)
    }

    fun addPlayer(playerName: String){
        var list = playerNames.value
        if(list==null){
            list = ArrayList()
        }
        list.add(playerName)
        playerNames.value = list
    }

    fun deletePlayer(index: Int){
        val list = playerNames.value
        list?.removeAt(index)
        playerNames.value = list
    }

    fun startGame(context: Activity){
        if((playerNames.value?.size ?: 0) < 2){
            Toast.makeText(
                context,
                "Please add at least two players.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val intent = Intent(context, GameActivity::class.java)
        gameSettings.playerList = playerNames.value ?: ArrayList<String>()
        intent.putExtra("GAME_SETTINGS", gameSettings)
        context.startActivity(intent)
    }
}

