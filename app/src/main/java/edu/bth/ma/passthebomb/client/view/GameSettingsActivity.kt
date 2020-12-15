package edu.bth.ma.passthebomb.client.view

import android.content.Intent
import android.os.Bundle
import android.widget.*
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.model.GameSettings

const val SEEK_BAR_MAX = 100
const val SEEK_BAR_DIVIDER = 100.0

class GameSettingsActivity : ActionBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_game_settings)

        title = "Game Settings"

        val challengeSetIds: ArrayList<String> = intent.getStringArrayListExtra("CHALLENGE_SET_IDS") ?: ArrayList<String>()


        val seekBarTimeModifier = findViewById<SeekBar>(R.id.seek_bar_time_modifier)
        seekBarTimeModifier.max = SEEK_BAR_MAX
        seekBarTimeModifier.progress = SEEK_BAR_MAX / 2
        val seekBarBombSensetivity = findViewById<SeekBar>(R.id.seek_bar_bomb_sensitivity)
        seekBarBombSensetivity.max = SEEK_BAR_MAX
        seekBarBombSensetivity.progress = SEEK_BAR_MAX / 2
        val switchRandomPlayerOrder = findViewById<Switch>(R.id.switch_choose_player_randomly)
        val switchEnableSound = findViewById<Switch>(R.id.switch_enable_sound)
        val editTextNumberRounds = findViewById<EditText>(R.id.edit_text_maximum_rounds)



        val button = findViewById<Button>(R.id.button_game_settings_start_game)
        button.setOnClickListener {
            var timeModifier: Double = (SEEK_BAR_MAX - seekBarTimeModifier.progress)/ SEEK_BAR_DIVIDER
            timeModifier *= 2
            var bombSensitivity: Double = seekBarBombSensetivity.progress / SEEK_BAR_DIVIDER
            bombSensitivity *= 2
            val shuffleRandomly: Boolean = switchRandomPlayerOrder.isChecked
            val enableSound: Boolean = switchEnableSound.isChecked
            val numberRounds: Int = 1
            try {
                editTextNumberRounds.text.toString().toInt()
            } catch (e: Exception) {
                Toast.makeText(this, "Invalid number of rounds!",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val gameSettings = GameSettings(challengeSetIds, ArrayList(), timeModifier,
                    bombSensitivity, shuffleRandomly, enableSound, numberRounds)
            val intent = Intent(this, AddPlayerActivity::class.java)
            intent.putExtra("GAME_SETTINGS", gameSettings)
            startActivity(intent)
        }
    }
}