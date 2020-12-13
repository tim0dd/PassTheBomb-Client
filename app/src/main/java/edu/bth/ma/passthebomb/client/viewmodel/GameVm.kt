package edu.bth.ma.passthebomb.client.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import edu.bth.ma.passthebomb.client.database.MockDatabase
import edu.bth.ma.passthebomb.client.model.Challenge
import edu.bth.ma.passthebomb.client.model.ChallengeSet
import edu.bth.ma.passthebomb.client.model.GameSettings
import edu.bth.ma.passthebomb.client.utils.CountDownTimerPausable
import edu.bth.ma.passthebomb.client.utils.RoundRobinScheduler
import kotlin.random.Random


enum class GameState{
    START,
    CHALLENGE,
    LEFT_PRESSED,
    LEFT_RIGHT_PRESSED,
    RIGHT_PRESSED,
    RIGHT_LEFT_PRESSED,
    KABOOM,
    PAUSED,
    GAME_OVER
}

const val ACCELERATION_THRESH_MULTIPLYER = 2.0
const val ATTENUATION_FACTOR = 0.5
var accelerationThresh: Double = ACCELERATION_THRESH_MULTIPLYER
lateinit var previousGameState: GameState

class GameVm(application: Application): DatabaseVm(application) {
    val gameSettings = MutableLiveData<GameSettings>()
    val challenges = ArrayList<Challenge>()

    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val playerName: MutableLiveData<String> = MutableLiveData()
    var secondsLeft: MutableLiveData<Float> = MutableLiveData()
    val currentChallenge: MutableLiveData<Challenge> = MutableLiveData()
    val gameState = MutableLiveData<GameState>()
    lateinit var countDownTimer: BombCountDownTimer
    lateinit var playerScores: ArrayList<Int>
    lateinit var playerScheduler: RoundRobinScheduler
    var relativeAcceleration = MutableLiveData<Double>(0.0)

    fun init(activity: AppCompatActivity, gameSettings: GameSettings){
        this.gameSettings.value = gameSettings
        isLoading.value = true
        playerScores = ArrayList(MutableList(gameSettings.playerList.size) { 0 })
        accelerationThresh = ACCELERATION_THRESH_MULTIPLYER / (gameSettings.bombSensitivity + 0.01)

        getChallengesByOverviewIds(gameSettings.challengeSetIds).observe(activity, Observer {
            challenges.addAll(it)
            isLoading.value = false
            start()
        })
    }

    fun start(){
        playerScheduler = RoundRobinScheduler(
            gameSettings.value!!.playerList.size,
            gameSettings.value!!.randomScheduling
        ) { round ->
            if (round >= gameSettings.value!!.numberRounds) {
                gameState.value = GameState.GAME_OVER
            }
        }
        playerName.value = gameSettings.value!!.playerList[playerScheduler.peekNextElement()]
        gameState.value = GameState.START
    }

    fun explode(){
        if(gameState.value == GameState.START || gameState.value == GameState.PAUSED
            || gameState.value == GameState.KABOOM){
            return
        }
        gameState.value = GameState.KABOOM
        playerScores[playerScheduler.currentValue] -= 100
    }

    fun startNewChallenge(){
        gameState.value = GameState.CHALLENGE
        val currentChallenge = challenges[Random.nextInt(challenges.size)]
        this.currentChallenge.value = currentChallenge
        secondsLeft.value = (currentChallenge.timeLimit * gameSettings.value!!.timeModifier).toFloat()
        val millisForChallenge: Long = (currentChallenge.timeLimit * 1000 * gameSettings.value!!.timeModifier).toLong()
        countDownTimer = BombCountDownTimer(millisForChallenge)
        playerScheduler.nextElement()
        countDownTimer.start()
    }

    fun currentTimeLimit(): Double {
        return (currentChallenge.value?.timeLimit ?: 0) * gameSettings.value!!.timeModifier
    }

    fun setSountEnabled(soundEnabled: Boolean){
        val gS = gameSettings.value!!
        val newGS = GameSettings(gS.challengeSetIds, gS.playerList, gS.timeModifier,
            gS.bombSensitivity, gS.randomScheduling, soundEnabled, gS.numberRounds)
        gameSettings.value = newGS
    }

    fun onRightButtonDown(){
        when(gameState.value){
            GameState.START -> startNewChallenge()
            GameState.CHALLENGE -> {
                playerName.value = gameSettings.value!!.playerList[playerScheduler.peekNextElement()]
                gameState.value = GameState.RIGHT_PRESSED
                countDownTimer.pause()
            }
            GameState.LEFT_PRESSED -> gameState.value = GameState.LEFT_RIGHT_PRESSED
        }
    }

    fun onRightButtonUp(){
        when(gameState.value){
            GameState.RIGHT_PRESSED -> {
                playerName.value = gameSettings.value!!.playerList[playerScheduler.currentValue]
                gameState.value = GameState.CHALLENGE
                countDownTimer.start()
            }
            GameState.LEFT_RIGHT_PRESSED -> explode()
            GameState.RIGHT_LEFT_PRESSED -> startNewChallenge()
        }
    }

    fun onLeftButtonDown(){
        when(gameState.value){
            GameState.START -> startNewChallenge()
            GameState.CHALLENGE -> {
                playerName.value = gameSettings.value!!.playerList[playerScheduler.peekNextElement()]
                gameState.value = GameState.LEFT_PRESSED
                countDownTimer.pause()
            }
            GameState.RIGHT_PRESSED -> gameState.value = GameState.RIGHT_LEFT_PRESSED
        }
    }

    fun onLeftButtonUp(){
        when(gameState.value){
            GameState.LEFT_PRESSED -> {
                playerName.value = gameSettings.value!!.playerList[playerScheduler.currentValue]
                gameState.value = GameState.CHALLENGE
                countDownTimer.start()
            }
            GameState.RIGHT_LEFT_PRESSED -> explode()
            GameState.LEFT_RIGHT_PRESSED -> startNewChallenge()
        }
    }

    fun onKaboomClick() {
        start()
    }

    fun onLinearAcceleration(x: Float, y: Float, z: Float): Double {
        val absoluteValue = Math.sqrt((x * x + y * y + z * z).toDouble())
        val relativeAcceleration = absoluteValue / accelerationThresh
        this.relativeAcceleration.value = relativeAcceleration * ATTENUATION_FACTOR +
                (this.relativeAcceleration.value?.times((1.0 - ATTENUATION_FACTOR)) ?: 0.0)
        if (relativeAcceleration > 1.0) {
            explode()
        }
        return relativeAcceleration
    }

    fun pauseGame() {
        if(gameState.value == GameState.PAUSED || gameState.value == GameState.START
            || gameState.value == GameState.KABOOM){
            return
        }
        previousGameState = gameState.value!!
        gameState.value = GameState.PAUSED
        countDownTimer.pause()
    }

    fun resumeGame(){
        if(gameState.value != GameState.PAUSED){
            return
        }
        gameState.value = previousGameState
        countDownTimer.start()
    }


    inner class BombCountDownTimer(millisInFuture: Long)
        : CountDownTimerPausable(millisInFuture, 100){
        override fun onFinish() {
            explode()
        }

        override fun onTick(millisUntilFinished: Long) {
            secondsLeft.value = secondsLeft.value?.minus(0.1f)
        }

    }
}







