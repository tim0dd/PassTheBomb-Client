package edu.bth.ma.passthebomb.client.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bth.ma.passthebomb.client.database.MockDatabase
import edu.bth.ma.passthebomb.client.model.Challenge
import edu.bth.ma.passthebomb.client.model.GameSettings
import edu.bth.ma.passthebomb.client.utils.CountDownTimerPausable
import kotlin.random.Random

enum class GameState{
    START,
    CHALLENGE,
    LEFT_PRESSED,
    LEFT_RIGHT_PRESSED,
    RIGHT_PRESSED,
    RIGHT_LEFT_PRESSED,
    KABOOM
}

class GameVm: ViewModel() {
    lateinit var gameSettings: GameSettings
    val challenges = ArrayList<Challenge>()

    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val playerName: MutableLiveData<String> = MutableLiveData()
    var secondsLeft: MutableLiveData<Float> = MutableLiveData()
    val currentChallenge: MutableLiveData<Challenge> = MutableLiveData()
    val gameState = MutableLiveData<GameState>()
    var currentPlayerPosition = 0
    var previousPlayerId: Int = 0
    lateinit var playerOrder: ArrayList<Int>
    lateinit var countDownTimer: BombCountDownTimer
    lateinit var playerScores: ArrayList<Int>

    fun init(gameSettings: GameSettings){
        this.gameSettings = gameSettings
        isLoading.value = true
        for(id in gameSettings.challengeSetIds){
            val challengeSet = MockDatabase().loadChallengeSet(id)
            challenges.addAll(challengeSet.challenges)
        }
        playerScores = ArrayList(MutableList(gameSettings.playerList.size) { 0 })
        isLoading.value = false
        start()
    }

    fun start(){
        newRound()
        playerName.value = gameSettings.playerList[currentPlayerId()]
        gameState.value = GameState.START
    }

    fun newRound(){
        playerOrder = (0..gameSettings.playerList.size-1).shuffled().toCollection(ArrayList<Int>())
        currentPlayerPosition = 0;
    }

    fun nextPlayer(){
        currentPlayerPosition ++
        if(currentPlayerPosition == gameSettings.playerList.size){
            newRound()
        }else{
            currentPlayerPosition++
        }
        playerName.value = gameSettings.playerList[currentPlayerId()]
    }

    fun explode(){
        gameState.value = GameState.KABOOM
        playerScores[currentPlayerId()] -= 100
    }

    fun startNewChallenge(){
        gameState.value = GameState.CHALLENGE
        val currentChallenge = challenges[Random.nextInt(challenges.size)]
        this.currentChallenge.value = currentChallenge
        secondsLeft.value = currentChallenge.timeLimit.toFloat()
        val millisForChallenge: Long = (currentChallenge.timeLimit * 1000 * gameSettings.timeModifier).toLong()
        countDownTimer = BombCountDownTimer(millisForChallenge)
        countDownTimer.start()
    }

    private fun currentPlayerId(): Int{
        return playerOrder[currentPlayerPosition]
    }

    fun currentTimeLimit(): Double {
        return (currentChallenge.value?.timeLimit ?: 0) * gameSettings.timeModifier
    }

    fun onRightButtonDown(){
        when(gameState.value){
            GameState.START -> startNewChallenge()
            GameState.CHALLENGE -> {
                previousPlayerId = currentPlayerId()
                gameState.value = GameState.RIGHT_PRESSED
                countDownTimer.pause()
                nextPlayer()
            }
            GameState.LEFT_PRESSED -> gameState.value = GameState.LEFT_RIGHT_PRESSED
        }
    }

    fun onRightButtonUp(){
        when(gameState.value){
            GameState.RIGHT_PRESSED -> {
                playerName.value = gameSettings.playerList[previousPlayerId]
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
                previousPlayerId = currentPlayerId()
                gameState.value = GameState.LEFT_PRESSED
                countDownTimer.pause()
                nextPlayer()
            }
            GameState.RIGHT_PRESSED -> gameState.value = GameState.RIGHT_LEFT_PRESSED
        }
    }

    fun onLeftButtonUp(){
        when(gameState.value){
            GameState.LEFT_PRESSED -> {
                playerName.value = gameSettings.playerList[previousPlayerId]
                gameState.value = GameState.CHALLENGE
                countDownTimer.start()
            }
            GameState.RIGHT_LEFT_PRESSED -> explode()
            GameState.LEFT_RIGHT_PRESSED -> startNewChallenge()
        }
    }

    fun onKaboomClick(){
        start()
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







