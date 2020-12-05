package edu.bth.ma.passthebomb.client.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.model.GameSettings

const val SEEK_BAR_MAX = 200
const val SEEK_BAR_DIVIDER = 100.0

class GameSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_game_settings)

        val challengeSetIds: ArrayList<String> = intent.getStringArrayListExtra("CHALLENGE_SET_IDS") ?: ArrayList<String>()


        val seekBarTimeModifier = findViewById<SeekBar>(R.id.seek_bar_time_modifier)
        seekBarTimeModifier.max = SEEK_BAR_MAX
        val seekBarBombSensetivity = findViewById<SeekBar>(R.id.seek_bar_bomb_sensitivity)
        seekBarBombSensetivity.max = SEEK_BAR_MAX



        val button = findViewById<Button>(R.id.button_game_settings_start_game)
        button.setOnClickListener {
            val timeModifier: Double = seekBarTimeModifier.progress / SEEK_BAR_DIVIDER
            val bombSensitivity: Double = seekBarBombSensetivity.progress / SEEK_BAR_DIVIDER
            val gameSettings = GameSettings(challengeSetIds, ArrayList<String>(), timeModifier, bombSensitivity)
            val intent = Intent(this, AddPlayerActivity::class.java)
            intent.putExtra("GAME_SETTINGS", gameSettings)
            startActivity(intent)
        }
    }
}