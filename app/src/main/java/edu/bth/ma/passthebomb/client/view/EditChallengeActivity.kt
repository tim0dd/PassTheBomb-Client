package edu.bth.ma.passthebomb.client.view

import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.model.Challenge
import edu.bth.ma.passthebomb.client.viewmodel.EditChallengeVm

class EditChallengeActivity : ActionBarActivity(){

    override val vm:EditChallengeVm by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_edit_challenge)

        title = "Edit Challenge"
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        val challengeId: String? = intent.getStringExtra("CHALLENGE_ID")
       if(challengeId==null){
           finish()
       }
        val editTextChallenge:EditText = findViewById(R.id.multiline_text_challenge_text)
        val seekBarChallengeTime: SeekBar = findViewById(R.id.seek_bar_challenge_time)
        val textViewTimeLimit:TextView = findViewById(R.id.text_view_challenge_time_limit)

        vm.getChallenge(challengeId ?: "").observe(this, Observer{
            if(it!=null){
                vm.init(it)
                editTextChallenge.setText(it.text)
            }
        })

        val saveButton: Button = findViewById(R.id.button_challenge_ok)
        saveButton.setOnClickListener{
            vm.setChallengeText(editTextChallenge.text.toString())
            val challenge = vm.challenge.value
            if(challenge!=null){
                if(challenge.text.trim().isNotEmpty()){
                    vm.updateChallenge(challenge)
                }else{
                    Toast.makeText(this,"No challenge text, deleting challenge!", Toast.LENGTH_SHORT).show();
                    vm.deleteChallenge(challenge.id)
                }
            }
            finish()
        }
        val cancelButton:Button = findViewById(R.id.button_challenge_cancel)
        cancelButton.setOnClickListener{
            val challenge = vm.challenge.value
            if(challenge!=null && challenge.text == ""){
                vm.deleteChallenge(challenge.id)
            }
            finish()
        }

        val deleteButton:Button = findViewById(R.id.button_challenge_delete)
        deleteButton.setOnClickListener {
            val challenge = vm.challenge.value
            if(challenge!=null){
                vm.deleteChallenge(challenge.id)
            }
            finish()
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