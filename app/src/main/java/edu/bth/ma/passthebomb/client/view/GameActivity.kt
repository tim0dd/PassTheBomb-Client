package edu.bth.ma.passthebomb.client.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.GridLabelRenderer.GridStyle
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.model.GameSettings
import edu.bth.ma.passthebomb.client.viewmodel.GameState
import edu.bth.ma.passthebomb.client.viewmodel.GameVm
import java.util.*


const val BOMB_GRAPH_UPDATE_INTERVAL: Long = 1000
const val BOMB_GRAPH_NUMBER_OF_VALUES: Int = 20

class GameActivity : AppCompatActivity(), SensorEventListener {


    val vm: GameVm by viewModels()

    //Acceleration Sensor
    lateinit var sensorManager: SensorManager
    lateinit var mainHandler: Handler
    lateinit var bombGraph: GraphView
    var sensor: Sensor? = null
    var count = 0.0
    var lastAccelerationValue = 0.0
    var relativeAccelerations = DoubleArray(BOMB_GRAPH_NUMBER_OF_VALUES) { 0.0 }

    val series: LineGraphSeries<DataPoint> = LineGraphSeries(
        relativeAccelerations.map { acc -> DataPoint(count++, acc) }.toTypedArray()
    )

    private val updateGraphTask = object : Runnable {
        override fun run() {
            updateGraph()
            mainHandler.postDelayed(this, BOMB_GRAPH_UPDATE_INTERVAL)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_game)

        //Wo do not want an action bar in the main game
        supportActionBar!!.hide()

        //register accelleration sensor
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)

        mainHandler = Handler(Looper.getMainLooper())

        val gameSettings: GameSettings? =
            intent.getSerializableExtra("GAME_SETTINGS") as GameSettings?
        if (gameSettings == null) {
            Toast.makeText(this, "No game settings, cannot start game this way", Toast.LENGTH_SHORT)
                .show()
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
        val slidingUpLayout = findViewById<SlidingUpPanelLayout>(R.id.sliding_up_layout_game)
        val buttonResume = findViewById<Button>(R.id.button_pause_resume)
        val buttonTutorial = findViewById<Button>(R.id.button_pause_tutorial)
        val buttonQuit = findViewById<Button>(R.id.button_pause_quit)

        bombGraph = findViewById(R.id.bombGraph)
        bombGraph.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {
            override fun formatLabel(value: Double, isValueX: Boolean): String {
                return ""
            }
        }
        bombGraph.gridLabelRenderer.gridStyle = GridStyle.NONE
        bombGraph.gridLabelRenderer.isHorizontalLabelsVisible = false
        bombGraph.gridLabelRenderer.isVerticalLabelsVisible = false
        bombGraph.viewport.isYAxisBoundsManual = true
        bombGraph.viewport.setMinX(0.0)
        bombGraph.viewport.setMaxX(1.0)
        val orange = resources.getColor(R.color.orange_main, theme)
        val bgColor = Color.argb(120, Color.red(orange), Color.green(orange), Color.blue(orange))
        //for some reason the java functions have to be used here
        series.isDrawBackground = true
        series.color = orange
        series.backgroundColor = bgColor
        bombGraph.addSeries(series)


        val timeObserver = Observer<Float> {
            progressBarTime.max = (vm.currentTimeLimit() * 10).toInt()
            progressBarTime.progress = (it * 10).toInt()
        }
        vm.secondsLeft.observe(this, timeObserver)

        val stateObserver = Observer<GameState> { state ->
            //kaboom and challenge visibility
            if (state == GameState.KABOOM) {
                val kaboomSound: MediaPlayer = MediaPlayer.create(this, R.raw.bomb)
                kaboomSound.start()
                constraint_layout_kaboom.visibility = View.VISIBLE
            } else {
                constraint_layout_kaboom.visibility = View.INVISIBLE
                if (state == GameState.CHALLENGE) {
                    textViewChallenge.visibility = View.VISIBLE
                } else {
                    textViewChallenge.visibility = View.INVISIBLE
                }
            }
            if(slidingUpLayout.panelState == SlidingUpPanelLayout.PanelState.EXPANDED &&
                state != GameState.PAUSED){
                slidingUpLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            }
            //button descriptions
            when(state){
                GameState.START -> {
                    buttonLeft.text = "Click One Side To Start"
                    buttonRight.text = "Click One Side To Start"
                    val mainHandler = Handler(Looper.getMainLooper())
                    mainHandler.post(updateGraphTask)

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
                GameState.GAME_OVER -> {
                    val intent = Intent(this, GameOverActivity::class.java)
                    intent.putExtra("GAME_SETTINGS", vm.gameSettings)
                    intent.putExtra("SCORES", vm.playerScores)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }
                GameState.PAUSED -> {
                    slidingUpLayout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
                }
            }
        }
        vm.gameState.observe(this, stateObserver)

        val accelerationObserver = Observer<Double> { acceleration ->
            val interpolated = ((1.0 - Math.min(acceleration, 1.0)) * 255).toInt()
            constraintLayoutGame.setBackgroundColor(
                Color.argb(
                    255,
                    255,
                    interpolated,
                    interpolated
                )
            )
        }
        vm.relativeAcceleration.observe(this, accelerationObserver)

        val nextPlayerObserver = Observer<String> { player ->
            textViewPlayer.text = "It's " + player + "'s turn" //TODO
        }
        vm.playerName.observe(this, nextPlayerObserver)

        buttonLeft.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> vm.onLeftButtonDown()
                MotionEvent.ACTION_UP -> vm.onLeftButtonUp()
            }
            true
        }

        buttonRight.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> vm.onRightButtonDown()
                MotionEvent.ACTION_UP -> vm.onRightButtonUp()
            }
            true
        }

        constraint_layout_kaboom.setOnClickListener{
            vm.onKaboomClick()
        }

        slidingUpLayout.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(p0: View?, p1: Float) {}

            override fun onPanelStateChanged(
                p0: View?,
                previousState: SlidingUpPanelLayout.PanelState?,
                newState: SlidingUpPanelLayout.PanelState?
            ) {
                if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    vm.pauseGame()
                }
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    vm.resumeGame()
                }
            }

        })

        buttonTutorial.setOnClickListener {
            val intent = Intent(this, TutorialActivity::class.java)
            startActivity(intent)
        }

        buttonResume.setOnClickListener {
            vm.resumeGame()
        }

        buttonQuit.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //TODO
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val mySensor: Sensor? = event?.sensor
        if (mySensor != null) {
            if (mySensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
                val x: Float = event.values[0]
                val y: Float = event.values[1]
                val z: Float = event.values[2]
                lastAccelerationValue = Math.min(vm.onLinearAcceleration(x, y, z), 1.0)
            }
        }
    }


    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        vm.pauseGame()
        mainHandler.removeCallbacks(updateGraphTask)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        vm.resumeGame()
        mainHandler.post(updateGraphTask)
    }

    fun updateGraph() {
        var count = 0.0
        /*   bombGraph.removeAllSeries()
           val series: LineGraphSeries<DataPoint> = LineGraphSeries<DataPoint>(
               relativeAccelerations.map { acc -> DataPoint(count++, acc) }.toTypedArray()
           )
           bombGraph.addSeries(series)*/
        val list = relativeAccelerations.toList()
        //rotate forward (last var becomes first one, others are shifted by 1)
        Collections.rotate(list, 1)
        relativeAccelerations = list.toDoubleArray()
        relativeAccelerations[0] = lastAccelerationValue
        series.resetData(relativeAccelerations.map { acc -> DataPoint(count++, acc) }
            .toTypedArray())
    }

}