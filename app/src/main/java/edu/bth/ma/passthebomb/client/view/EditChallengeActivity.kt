package edu.bth.ma.passthebomb.client.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.model.Challenge
import edu.bth.ma.passthebomb.client.viewmodel.EditChallengeVm

class EditChallengeActivity : AppCompatActivity() {

    val vm:EditChallengeVm by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_edit_challenge)

        val editTextChallenge:EditText = findViewById(R.id.multiline_text_challenge_text)
        val seekBarChallengeTime: SeekBar = findViewById(R.id.seek_bar_challenge_time)
        val textViewTimeLimit:TextView = findViewById(R.id.text_view_challenge_time_limit)

        val saveButton: Button = findViewById(R.id.button_challenge_ok)
        saveButton.setOnClickListener{
            vm.onSave(this)
        }
        val cancelButton:Button = findViewById(R.id.button_challenge_cancel)
        cancelButton.setOnClickListener{
            vm.onCancel(this)
        }

        val challengeObserver = Observer<Challenge> {
            textViewTimeLimit.text = it.timeLimit.toString() + " sec"
            editTextChallenge.setText(it.text)
            //seekBarChallengeTime.progress = vm.getChallengeTimeAsSliderProgress()
        }

    }
}