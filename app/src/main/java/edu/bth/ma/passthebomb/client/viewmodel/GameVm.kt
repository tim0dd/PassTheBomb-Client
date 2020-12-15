package edu.bth.ma.passthebomb.client.viewmodel

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

const val ACCELERATION_THRESH_MULTIPLIER = 2.0
const val ATTENUATION_FACTOR = 0.5
const val CHALLENGED_SOLVED_SCORE = 50
const val BOMB_EXPLOSION_SCORE = -100
var accelerationThresh: Double = ACCELERATION_THRESH_MULTIPLIER
lateinit var previousGameState: GameState

class GameVm : ViewModel() {
    val gameSettings = MutableLiveData<GameSettings>()

    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val playerName: MutableLiveData<String> = MutableLiveData()
    var secondsLeft: MutableLiveData<Float> = MutableLiveData()
    val currentChallenge: MutableLiveData<Challenge> = MutableLiveData()
    val gameState = MutableLiveData<GameState>()
    private lateinit var challenges: ArrayList<Challenge>
    var relativeAcceleration = MutableLiveData<Double>(0.0)
    private var countDownTimer: BombCountDownTimer? = null
    lateinit var playerScores: ArrayList<Int>
    private lateinit var playerScheduler: RoundRobinScheduler
    private var currentChallengeIndex = -1

    fun init(gameSettings: GameSettings, challenges: ArrayList<Challenge>) {
        this.gameSettings.value = gameSettings
        isLoading.value = false
        playerScores = ArrayList(MutableList(gameSettings.playerList.size) { 0 })
        accelerationThresh = ACCELERATION_THRESH_MULTIPLIER / (gameSettings.bombSensitivity + 0.01)
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
        playerName.value = gameSettings.value!!.playerList[playerScheduler.currentElement]
        gameState.value = GameState.START
    }

    fun explode() {
        if (gameState.value == GameState.START || gameState.value == GameState.PAUSED
            || gameState.value == GameState.KABOOM
        ) {
            return
        }
        gameState.value = GameState.KABOOM
        countDownTimer?.pause() //stop and destroys the timer
        countDownTimer = null
        secondsLeft.value = 0f
        playerScores[playerScheduler.currentElement] += BOMB_EXPLOSION_SCORE
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
        countDownTimer?.start()
    }

    fun challengeSolved() {
        playerScores[playerScheduler.currentElement] += CHALLENGED_SOLVED_SCORE
        playerScheduler.nextElement()
        startNewChallenge()
    }

    fun currentTimeLimit(): Double {
        return (currentChallenge.value?.timeLimit ?: 0) * gameSettings.value!!.timeModifier
    }

    fun setSoundEnabled(soundEnabled: Boolean){
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
                playerName.value = gameSettings.value!!.playerList[playerScheduler.currentElement]
                gameState.value = GameState.CHALLENGE
                countDownTimer?.start()
            }
            GameState.LEFT_RIGHT_PRESSED -> explode()
            GameState.RIGHT_LEFT_PRESSED -> challengeSolved()
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
                playerName.value = gameSettings.value!!.playerList[playerScheduler.currentElement]
                gameState.value = GameState.CHALLENGE
                countDownTimer?.start()
            }
            GameState.RIGHT_LEFT_PRESSED -> explode()
            GameState.LEFT_RIGHT_PRESSED -> challengeSolved()
            else -> {}
        }
    }

    fun restartAfterKaboom() {
        if(gameState.value == GameState.KABOOM){
            playerScheduler.nextElement()
            restart()
        }
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

