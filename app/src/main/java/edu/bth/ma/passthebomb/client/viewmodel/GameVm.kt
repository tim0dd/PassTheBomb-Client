package edu.bth.ma.passthebomb.client.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bth.ma.passthebomb.client.model.Challenge
import edu.bth.ma.passthebomb.client.model.GameSettings
import edu.bth.ma.passthebomb.client.utils.CountDownTimerPausable
import edu.bth.ma.passthebomb.client.utils.RoundRobinScheduler


enum class GameState {
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

class GameVm : ViewModel() {
    val gameSettings = MutableLiveData<GameSettings>()
    lateinit var challenges: ArrayList<Challenge>

    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val playerName: MutableLiveData<String> = MutableLiveData()
    var secondsLeft: MutableLiveData<Float> = MutableLiveData()
    val currentChallenge: MutableLiveData<Challenge> = MutableLiveData()
    val gameState = MutableLiveData<GameState>()
    var countDownTimer: BombCountDownTimer? = null
    lateinit var playerScores: ArrayList<Int>
    lateinit var playerScheduler: RoundRobinScheduler
    var relativeAcceleration = MutableLiveData<Double>(0.0)
    var currentChallengeIndex = -1

    fun init(gameSettings: GameSettings, challenges: ArrayList<Challenge>) {
        this.gameSettings.value = gameSettings
        isLoading.value = false
        playerScores = ArrayList(MutableList(gameSettings.playerList.size) { 0 })
        accelerationThresh = ACCELERATION_THRESH_MULTIPLYER / (gameSettings.bombSensitivity + 0.01)
        this.challenges = arrayListOf()
        this.challenges.addAll(challenges)
        this.challenges.shuffle()
        currentChallenge.value = challenges[0]
        start()
    }

    fun start() {
        playerScheduler = RoundRobinScheduler(
            gameSettings.value!!.playerList.size,
            gameSettings.value!!.randomScheduling
        ) { round ->
            if (round >= gameSettings.value!!.numberRounds) {
                gameState.value = GameState.GAME_OVER
            }
            this.challenges.shuffle()
        }
        restart()
    }

    fun restart() {
        playerName.value = gameSettings.value!!.playerList[playerScheduler.peekNextElement()]
        gameState.value = GameState.START
    }

    fun explode() {
        if (gameState.value == GameState.START || gameState.value == GameState.PAUSED
            || gameState.value == GameState.KABOOM
        ) {
            return
        }
        gameState.value = GameState.KABOOM
        countDownTimer = null //stops and destroys the timer
        playerScores[playerScheduler.currentValue] -= 100
    }

    fun startNewChallenge() {
        gameState.value = GameState.CHALLENGE
        currentChallengeIndex++
        if (currentChallengeIndex == challenges.size) currentChallengeIndex = 0
        val currentChallenge = challenges[currentChallengeIndex]
        this.currentChallenge.value = currentChallenge
        secondsLeft.value = (currentChallenge.timeLimit * gameSettings.value!!.timeModifier).toFloat()
        val millisForChallenge: Long = (currentChallenge.timeLimit * 1000 * gameSettings.value!!.timeModifier).toLong()
        countDownTimer = BombCountDownTimer(millisForChallenge)
        playerScheduler.nextElement()
        countDownTimer?.start()
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

    fun onRightButtonDown() {
        when (gameState.value) {
            GameState.START -> startNewChallenge()
            GameState.CHALLENGE -> {
                playerName.value = gameSettings.value!!.playerList[playerScheduler.peekNextElement()]
                gameState.value = GameState.RIGHT_PRESSED
                countDownTimer?.pause()
            }
            GameState.LEFT_PRESSED -> gameState.value = GameState.LEFT_RIGHT_PRESSED
            else -> {}
        }
    }

    fun onRightButtonUp() {
        when (gameState.value) {
            GameState.RIGHT_PRESSED -> {
                playerName.value = gameSettings.value!!.playerList[playerScheduler.currentValue]
                gameState.value = GameState.CHALLENGE
                countDownTimer?.start()
            }
            GameState.LEFT_RIGHT_PRESSED -> explode()
            GameState.RIGHT_LEFT_PRESSED -> startNewChallenge()
            else -> {}
        }
    }

    fun onLeftButtonDown() {
        when (gameState.value) {
            GameState.START -> startNewChallenge()
            GameState.CHALLENGE -> {
                playerName.value = gameSettings.value!!.playerList[playerScheduler.peekNextElement()]
                gameState.value = GameState.LEFT_PRESSED
                countDownTimer?.pause()
            }
            GameState.RIGHT_PRESSED -> gameState.value = GameState.RIGHT_LEFT_PRESSED
            else -> {}

        }
    }

    fun onLeftButtonUp() {
        when (gameState.value) {
            GameState.LEFT_PRESSED -> {
                playerName.value = gameSettings.value!!.playerList[playerScheduler.currentValue]
                gameState.value = GameState.CHALLENGE
                countDownTimer?.start()
            }
            GameState.RIGHT_LEFT_PRESSED -> explode()
            GameState.LEFT_RIGHT_PRESSED -> startNewChallenge()
            else -> {}
        }
    }

    fun onKaboomClick() {
        restart()
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
        if (gameState.value == GameState.PAUSED || gameState.value == GameState.START
            || gameState.value == GameState.KABOOM
        ) {
            return
        }
        previousGameState = gameState.value!!
        gameState.value = GameState.PAUSED
        countDownTimer?.pause()
    }

    fun resumeGame() {
        if (gameState.value != GameState.PAUSED) {
            return
        }
        gameState.value = previousGameState
        countDownTimer?.start()
    }


    inner class BombCountDownTimer(millisInFuture: Long) :
        CountDownTimerPausable(millisInFuture, 100) {
        override fun onFinish() {
            explode()
        }

        override fun onTick(millisUntilFinished: Long) {
            secondsLeft.value = secondsLeft.value?.minus(0.1f)
        }

    }
}

