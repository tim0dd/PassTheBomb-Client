package edu.bth.ma.passthebomb.client.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.view.adapter.PlayerListAdapter

class GameSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_settings)

        val button = findViewById<Button>(R.id.button_game_settings_start_game)
        button.setOnClickListener {
            val intent = Intent(this, AddPlayerActivity::class.java).apply {  }
            startActivity(intent)
        }
    }
}