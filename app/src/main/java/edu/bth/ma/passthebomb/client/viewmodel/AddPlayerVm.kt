package edu.bth.ma.passthebomb.client.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Parcelable
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.bth.ma.passthebomb.client.model.Challenge
import edu.bth.ma.passthebomb.client.model.GameSettings
import edu.bth.ma.passthebomb.client.utils.ObserveExtensions.Companion.observeOnce
import edu.bth.ma.passthebomb.client.view.GameActivity

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
    }

    fun addPlayer(playerName: String) {
        var list = playerNames.value
        if (list == null) {
            list = ArrayList()
        }
        list.add(playerName)
        playerNames.value = list
    }

    fun deletePlayer(index: Int) {
        val list = playerNames.value
        list?.removeAt(index)
        playerNames.value = list
    }

    fun startGame(context: Activity) {
        if ((playerNames.value?.size ?: 0) < 2) {
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

        //fetching challenges should be easily done by now, but we can make sure to wait if it is not
        while (challengesLiveData.hasActiveObservers()) {
            Thread.sleep(20)
        }
        if (challenges.isNotEmpty()) {
            val list = arrayListOf<Parcelable>()
            list.addAll(challenges)
            intent.putParcelableArrayListExtra("challenges", list)
            context.startActivity(intent)
        } else {
            Toast.makeText(
                context,
                "No challenges found in selected challenge sets",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}

