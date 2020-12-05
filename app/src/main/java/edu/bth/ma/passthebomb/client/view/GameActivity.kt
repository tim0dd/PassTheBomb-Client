package edu.bth.ma.passthebomb.client.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Interpolator
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.model.GameSettings
import edu.bth.ma.passthebomb.client.viewmodel.GameState
import edu.bth.ma.passthebomb.client.viewmodel.GameVm

class GameActivity : AppCompatActivity(), SensorEventListener {

    val vm: GameVm by viewModels()

    //Acceleration Sensor
    lateinit var sensorManager: SensorManager
    var sensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_game)

        //Wo do not want an action bar in the main game
        getSupportActionBar()!!.hide();

        //register accelleration sensor
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)

        val gameSettings: GameSettings? = intent.getSerializableExtra("GAME_SETTINGS") as GameSettings?
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
        val constraint_layout_kaboom = findViewById<ConstraintLayout>(R.id.constraint_layout_boom)
        val constraintLayoutGame = findViewById<ConstraintLayout>(R.id.constraint_layout_game)

        val timeObserver = Observer<Float> {
            progressBarTime.max = (vm.currentTimeLimit() * 10).toInt()
            progressBarTime.progress = (it * 10).toInt()
        }
        vm.secondsLeft.observe(this, timeObserver)

        val stateObserver = Observer<GameState> {state ->
            //kaboom and challenge visibility
            if(state == GameState.KABOOM){
                constraint_layout_kaboom.visibility = View.VISIBLE
            }else{
                constraint_layout_kaboom.visibility = View.INVISIBLE
                if(state == GameState.CHALLENGE){
                    textViewChallenge.visibility = View.VISIBLE
                }else{
                    textViewChallenge.visibility = View.INVISIBLE
                }
            }
            //button descriptions
            when(state){
                GameState.START -> {
                    buttonLeft.text = "Click One Side To Start"
                    buttonRight.text = "Click One Side To Start"

                }
                GameState.CHALLENGE -> {
                    buttonLeft.text = "Press One Side When Finished"
                    buttonRight.text = "Press One Side When Finished"
                }
                GameState.LEFT_PRESSED -> {
                    buttonLeft.text = "Hold Until Next Player Presses Right"
                    buttonRight.text = "${vm.playerName.value} Touch To Take Bomb"

                }
                GameState.RIGHT_PRESSED -> {
                    buttonLeft.text = "${vm.playerName.value} Touch To Take Bomb"
                    buttonRight.text = "Hold Until Next Player Presses Left"
                }
                GameState.LEFT_RIGHT_PRESSED -> {
                    buttonLeft.text = "Release To Pass On Bomb"
                    buttonRight.text = "Hold Until Left Is Released"
                }
                GameState.RIGHT_LEFT_PRESSED -> {
                    buttonRight.text = "Release To Pass On Bomb"
                    buttonLeft.text = "Hold Until Right Is Released"
                }
                GameState.GAME_OVER ->{
                    val intent = Intent(this, GameOverActivity::class.java)
                    intent.putExtra("GAME_SETTINGS", vm.gameSettings)
                    intent.putExtra("SCORES", vm.playerScores)
                    startActivity(intent)
                }
            }
        }
        vm.gameState.observe(this, stateObserver)

        val accelerationObserver = Observer<Double> {acceleration ->
            val interpolated = ((1.0 - Math.min(acceleration, 1.0)) * 255).toInt()
            constraintLayoutGame.setBackgroundColor(Color.argb(255, 255, interpolated, interpolated))
        }
        vm.relativeAcceleration.observe(this, accelerationObserver)

        val nextPlayerObserver = Observer<String> { player ->
            textViewPlayer.text = "It's " + player + "'s turn" //TODO
        }
        vm.playerName.observe(this, nextPlayerObserver)

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

        constraint_layout_kaboom.setOnClickListener{
            vm.onKaboomClick()
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //TODO
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val mySensor: Sensor? = event?.sensor
        if (mySensor != null) {
            if (mySensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
                val x: Float = event.values.get(0)
                val y: Float = event.values.get(1)
                val z: Float = event.values.get(2)
                vm.onLinearAcceleration(x,y,z)
            }
        }
    }

    override fun onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    override fun onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

}