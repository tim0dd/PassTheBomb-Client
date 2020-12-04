package edu.bth.ma.passthebomb.client.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import edu.bth.ma.passthebomb.client.R

class GameSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_game_settings)

        val challengeSetIds: ArrayList<String>? = intent.getStringArrayListExtra("CHALLENGE_SET_IDS")

        val button = findViewById<Button>(R.id.button_game_settings_start_game)
        button.setOnClickListener {

            val gameSettings = GameSettings
            val intent = Intent(this, AddPlayerActivity::class.java)
            startActivity(intent)
        }
    }
}