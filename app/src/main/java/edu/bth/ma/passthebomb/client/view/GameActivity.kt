package edu.bth.ma.passthebomb.client.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.*
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*
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
import edu.bth.ma.passthebomb.client.model.Challenge
import edu.bth.ma.passthebomb.client.model.GameSettings
import edu.bth.ma.passthebomb.client.viewmodel.GameState
import edu.bth.ma.passthebomb.client.viewmodel.GameVm
import java.util.*

const val BOMB_GRAPH_UPDATE_INTERVAL: Long = 100
const val BOMB_GRAPH_NUMBER_OF_VALUES: Int = 200

class GameActivity : AppCompatActivity(), SensorEventListener {
    val vm: GameVm by viewModels()

    //Acceleration Sensor
    lateinit var sensorManager: SensorManager
    lateinit var mainHandler: Handler
    lateinit var bombGraph: GraphView
    lateinit var vibrator: Vibrator
    var sensor: Sensor? = null
    var count = 0.0
    var lastAccelerationValue = 0.0
    var relativeAccelerations = DoubleArray(BOMB_GRAPH_NUMBER_OF_VALUES) { 0.0 }

    lateinit var challenges: ArrayList<Challenge>

    private val series: LineGraphSeries<DataPoint> = LineGraphSeries(
        relativeAccelerations.map { acc -> DataPoint(count++, acc) }.toTypedArray()
    )

    private val updateGraphTask = object : Runnable {
        override fun run() {
            updateGraph()
            mainHandler.postDelayed(this, BOMB_GRAPH_UPDATE_INTERVAL)
        }
    }

    override fun onAttachedToWindow() {
        //disable notification bar
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.screen_game)

        //prevent screen sleep during game
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        //Wo do not want an action bar in the main game
        supportActionBar!!.hide()

        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator

        //register acceleration sensor
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
            return
        }

        val challengesMaybeNull: ArrayList<Challenge>? =
            intent.getParcelableArrayListExtra("challenges")
        if (challengesMaybeNull == null || challengesMaybeNull.isEmpty()) {
            Toast.makeText(
                this,
                "No challenges available, cannot start game this way",
                Toast.LENGTH_SHORT
            )
                .show()
            finish()
            return
        }
        challenges = challengesMaybeNull
        vm.init(gameSettings, challenges)

        val buttonLeft = findViewById<Button>(R.id.button_game_left)
        val buttonRight = findViewById<Button>(R.id.button_game_right)
        val textViewPlayer = findViewById<TextView>(R.id.text_view_game_player)
        val textViewChallenge = findViewById<TextView>(R.id.text_view_game_challenge)
        val progressBarTime = findViewById<ProgressBar>(R.id.prograss_bar_game_time)
        val constraintLayoutKaboom = findViewById<ConstraintLayout>(R.id.constraint_layout_boom)
        val imageViewOverlay = findViewById<ImageView>(R.id.image_view_game_overlay)
        val slidingUpLayout = findViewById<SlidingUpPanelLayout>(R.id.sliding_up_layout_game)
        val buttonResume = findViewById<Button>(R.id.button_pause_resume)
        val buttonTutorial = findViewById<Button>(R.id.button_pause_tutorial)
        val buttonQuit = findViewById<Button>(R.id.button_pause_quit)
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar_game)
        val switchSoundEnable = findViewById<Switch>(R.id.switch_game_sound_effects)
        val bombFire = findViewById<ImageView>(R.id.bomb_fire)


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
        val teal = resources.getColor(R.color.teal_main, theme)
        val bgColor = Color.argb(120, Color.red(teal), Color.green(teal), Color.blue(teal))
        series.isDrawBackground = true
        series.color = teal
        series.backgroundColor = bgColor
        bombGraph.addSeries(series)

        val timeObserver = Observer<Float> {
            //animate once per second if ((it % .1) in 0.09..0.11)
            animateFire(bombFire)
            progressBarTime.max = (vm.currentTimeLimit() * 10).toInt()
            progressBarTime.progress = (it * 10).toInt()
        }
        vm.secondsLeft.observe(this, timeObserver)

        val gameSettingsObjserver = Observer<GameSettings> {
            switchSoundEnable.isChecked = it.enableSound
        }
        vm.gameSettings.observe(this, gameSettingsObjserver)
        switchSoundEnable.setOnCheckedChangeListener { buttonView, isChecked ->
            vm.setSoundEnabled(isChecked)
        }

        val stateObserver = Observer<GameState> { state ->
            //kaboom and challenge visibility
            if (state == GameState.KABOOM) {
                if (vm.gameSettings.value?.enableSound == true) {
                    val kaboomSound: MediaPlayer = MediaPlayer.create(this, R.raw.bomb)
                    kaboomSound.start()
                }
                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            1000,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(1000)
                }
                constraintLayoutKaboom.visibility = View.VISIBLE
            } else {
                constraintLayoutKaboom.visibility = View.INVISIBLE
                if (state == GameState.CHALLENGE) {
                    textViewChallenge.visibility = View.VISIBLE
                } else {
                    textViewChallenge.visibility = View.INVISIBLE
                }
            }
            if (slidingUpLayout.panelState == SlidingUpPanelLayout.PanelState.EXPANDED &&
                state != GameState.PAUSED
            ) {
                slidingUpLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            }
            //button descriptions
            when (state) {
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
                    intent.putExtra("GAME_SETTINGS", vm.gameSettings.value!!)
                    intent.putExtra("SCORES", vm.playerScores)
                    // put challenges in intent in case the game is restarted
                    val list = arrayListOf<Parcelable>()
                    list.addAll(challenges)
                    intent.putParcelableArrayListExtra("challenges", list)
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
            val interpolated = (Math.min(acceleration, 1.0) * 200).toInt()
            imageViewOverlay.setBackgroundColor(
                Color.argb(
                    interpolated,
                    255,
                    0,
                    0
                )
            )
        }
        vm.relativeAcceleration.observe(this, accelerationObserver)

        val nextPlayerObserver = Observer<String> { player ->
            var text: String = ""
            text = if(player.endsWith("s")){
                "It's ${player}' turn"
            }else{
                "It's ${player}'s turn"
            }
            textViewPlayer.text = text
        }
        vm.playerName.observe(this, nextPlayerObserver)

        val challengeObserver = Observer<Challenge> {
            textViewChallenge.text = it.text
        }
        vm.currentChallenge.observe(this, challengeObserver)

        val loadingObserver = Observer<Boolean> {
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
        vm.isLoading.observe(this, loadingObserver)

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

        constraintLayoutKaboom.setOnClickListener {
            vm.restartAfterKaboom()
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
            if (slidingUpLayout.panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                slidingUpLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            }
        }

        buttonQuit.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

    }

    //needs to be implemented to fulfill the interface but we do not need it
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

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
        val list = relativeAccelerations.toList()
        //rotate forward (last var becomes first one, others are shifted by 1)
        Collections.rotate(list, 1)
        relativeAccelerations = list.toDoubleArray()
        relativeAccelerations[0] = (lastAccelerationValue + relativeAccelerations[1]) / 2
        series.resetData(relativeAccelerations.map { acc -> DataPoint(count++, acc) }
            .toTypedArray())
    }

    fun animateFire(bombFire: ImageView) {
        bombFire.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake)); }
}