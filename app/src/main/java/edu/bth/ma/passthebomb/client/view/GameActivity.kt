package edu.bth.ma.passthebomb.client.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.model.GameSettings
import edu.bth.ma.passthebomb.client.viewmodel.GameState
import edu.bth.ma.passthebomb.client.viewmodel.GameVm

class GameActivity : AppCompatActivity() {

    val vm: GameVm by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_game)

        val gameSettings: GameSettings? = getIntent().getSerializableExtra("GAME_SETTINGS") as GameSettings?
        if(gameSettings==null) {
            Toast.makeText(this, "No game settings, cannot start game this way", Toast.LENGTH_SHORT).show()
            finish()
        }
        vm.init(gameSettings!!)

        val buttonLeft = findViewById<Button>(R.id.button_game_left)
        val buttonRight = findViewById<Button>(R.id.button_game_right)
        val textViewPlayer = findViewById<TextView>(R.id.text_view_game_player)
        val textViewChallenge = findViewById<TextView>(R.id.text_view_game_challenge)
        val progressBarTime = findViewById<ProgressBar>(R.id.prograss_bar_game_time)
        val imageViewBoom = findViewById<ImageView>(R.id.image_view_boom)
        val textViewBoom = findViewById<TextView>(R.id.text_view_tap_to_continue)

        val timeObserver = Observer<Float> {
            progressBarTime.max = (vm.currentTimeLimit() * 10).toInt()
            progressBarTime.progress = (it * 10).toInt()
        }
        vm.secondsLeft.observe(this, timeObserver)

        val stateObserver = Observer<GameState> {state ->
            if(state == GameState.KABOOM){
                imageViewBoom.visibility = View.VISIBLE
                textViewBoom.visibility = View.VISIBLE
            }else{
                imageViewBoom.visibility = View.INVISIBLE
                textViewBoom.visibility = View.INVISIBLE
                if(state == GameState.CHALLENGE){
                    textViewChallenge.visibility = View.VISIBLE
                }else{
                    textViewChallenge.visibility = View.INVISIBLE
                }
            }
        }
        vm.gameState.observe(this, stateObserver)

        val nextPlayerObserver = Observer<String> { player ->
            textViewPlayer.text = "It's " + player + "'s turn" //TODO
        }

        buttonLeft.setOnTouchListener{
        _, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> vm.onLeftButtonDown()
                MotionEvent.ACTION_UP -> vm.onLeftButtonUp()
            }
            true
        }

        buttonRight.setOnTouchListener{
                _, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> vm.onRightButtonDown()
                MotionEvent.ACTION_UP -> vm.onRightButtonUp()
            }
            true
        }

    }

}