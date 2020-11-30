package edu.bth.ma.passthebomb.client.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import edu.bth.ma.passthebomb.client.R

class MainScreen : AppCompatActivity() {

    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        button = findViewById(R.id.buttonMyChallengeSets)

        button.setOnClickListener {
            buttonMyChallengeSetsPressed()
        }
    }

    fun buttonMyChallengeSetsPressed(){
        val intent = Intent(this, MyChallengeSetsScreen::class.java).apply {  }
        startActivity(intent)
    }
}