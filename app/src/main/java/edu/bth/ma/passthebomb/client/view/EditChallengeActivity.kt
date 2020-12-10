package edu.bth.ma.passthebomb.client.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.model.Challenge
import edu.bth.ma.passthebomb.client.viewmodel.EditChallengeVm

class EditChallengeActivity : ActionBarActivity(){

    val vm:EditChallengeVm by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_edit_challenge)

        title = "Edit Challenge"
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        val challengeSetIdUnsafe: String? = getIntent().getStringExtra("CHALLENGE_SET_ID")
        val challengeId: Int = getIntent().getIntExtra("CHALLENGE_ID", -1)
        var challengeSetId: String
        if (challengeSetIdUnsafe != null) {
            challengeSetId = challengeSetIdUnsafe
        } else {
            finish()
            challengeSetId = ""
        }
        vm.initChallenge(challengeSetId, challengeId)

        val editTextChallenge:EditText = findViewById(R.id.multiline_text_challenge_text)
        val challengeText: String = vm.challenge.value?.text ?: ""
        editTextChallenge.setText(challengeText)
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

        seekBarChallengeTime.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    vm.setTimeLimit(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val challengeObserver = Observer<Challenge> {
            textViewTimeLimit.text = it.timeLimit.toString() + " sec"
            seekBarChallengeTime.progress = it.timeLimit
        }
        vm.challenge.observe(this, challengeObserver)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            android.R.id.home -> {
                // API 5+ solution
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}