package edu.bth.ma.passthebomb.client.view

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import edu.bth.ma.passthebomb.client.R


class TutorialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_tutorial)

        val textView: TextView = findViewById(R.id.text_view_tutorial)
        textView.setMovementMethod(ScrollingMovementMethod())

        val buttonMyChallengeSets: Button = findViewById(R.id.button_close_tutorial)
        buttonMyChallengeSets.setOnClickListener {
            finish()
        }
    }
}