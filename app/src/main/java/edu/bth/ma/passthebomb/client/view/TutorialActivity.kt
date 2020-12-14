package edu.bth.ma.passthebomb.client.view

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.TextView
import edu.bth.ma.passthebomb.client.R


class TutorialActivity : ActionBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_tutorial)

        title = "Tutorial"

        val textView: TextView = findViewById(R.id.text_view_tutorial)
        textView.movementMethod = ScrollingMovementMethod()

        val buttonMyChallengeSets: Button = findViewById(R.id.button_close_tutorial)
        buttonMyChallengeSets.setOnClickListener {
            finish()
        }
    }
}