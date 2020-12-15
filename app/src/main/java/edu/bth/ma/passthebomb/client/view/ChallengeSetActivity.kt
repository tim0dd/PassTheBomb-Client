package edu.bth.ma.passthebomb.client.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.model.Challenge
import edu.bth.ma.passthebomb.client.remote.RestService
import edu.bth.ma.passthebomb.client.utils.IdGenerator
import edu.bth.ma.passthebomb.client.viewmodel.ChallengeSetVm
import java.util.*

class ChallengeSetActivity : ActionBarActivity() {
    override val vm by viewModels<ChallengeSetVm>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_challenge_set)

        title = "Challenge Set"

        val challengeSetIdUnsafe: String? = intent.getStringExtra("CHALLENGE_SET_ID")
        lateinit var challengeSetId: String
        challengeSetId = if (challengeSetIdUnsafe == null) {
            finish()
            ""
        } else {
            challengeSetIdUnsafe
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_challenge_list)
        val buttonSynchronize = findViewById<MaterialButton>(R.id.button_upload_challenge_set)
        val addChallengeButton: Button = findViewById(R.id.button_add_challenge)
        val buttonDeleteChallengeSet = findViewById<Button>(R.id.button_delete_challenge_set)

        vm.init(challengeSetId)
        vm.challengeSet.observe(this, androidx.lifecycle.Observer {
            if(it.isOwnChallengeSet(this)){
                title = "Own Set: " + it?.challengeSetOverview?.name
            }else if(it.isBundledChallengeSet()){
                title = "Bundled Set: " + it?.challengeSetOverview?.name
            }else if(it.isDownloadedChallengeSet(this)){
                title = "Downloaded Set: " + it?.challengeSetOverview?.name
            }else{
                title = "Challenge Set: " + it?.challengeSetOverview?.name
            }
            if(it.isBundledChallengeSet()){
                buttonSynchronize.visibility = View.GONE
                buttonDeleteChallengeSet.visibility = View.GONE
            }
            if (it != null) {
                recyclerView.adapter =
                    ChallengeListAdapter(
                        this,
                        it.challenges
                    )
                recyclerView.setHasFixedSize(true)
                if (it.isOwnChallengeSet(this)) {
                    if (it.challengeSetOverview.uploadedDate != null) {
                        buttonSynchronize.text = "Reupload"
                    } else {
                        buttonSynchronize.text = "Upload"
                    }
                    buttonSynchronize.setIconResource(R.drawable.ic_upload)
                } else {
                    buttonSynchronize.text = "Redownload"
                    buttonSynchronize.setIconResource(R.drawable.ic_download)
                }
            }
        })

        addChallengeButton.setOnClickListener {
            val intent = Intent(this, EditChallengeActivity::class.java)
            val newChallengeId = IdGenerator().generateDbId()
            val challenge = Challenge(newChallengeId, challengeSetId, Date(), "", 60)
            vm.addChallenge(challenge)
            intent.putExtra("CHALLENGE_ID", newChallengeId)
            this.startActivity(intent)
        }

        buttonSynchronize.setOnClickListener {
            val challengeSet = vm.challengeSet.value
            if (challengeSet != null) {
                val restService = RestService(this)
                if(challengeSet.isOwnChallengeSet(this)){
                    restService.uploadChallengeSet(challengeSet, {
                        Toast.makeText(
                            this,
                            "Successfully uploaded challenge set.",
                            Toast.LENGTH_SHORT
                        ).show()
                        //set new uploaded date
                        challengeSet.challengeSetOverview.uploadedDate = Date()
                        vm.updateChallengeSet(challengeSet)
                    }, {
                        Toast.makeText(
                            this,
                            "Could not upload the challenge set.",
                            Toast.LENGTH_SHORT
                        ).show()
                    })
                }else{
                    restService.getChallengeSet(challengeSet!!.challengeSetOverview.id,{
                        vm.updateChallengeSet(it)
                        Toast.makeText(
                            this,
                            "Updated challenge set from server.",
                            Toast.LENGTH_SHORT
                        ).show()
                    },{
                        Toast.makeText(
                            this,
                            "Could not update challenge set from server.",
                            Toast.LENGTH_SHORT
                        ).show()
                    })
                }
            }
        }

        buttonDeleteChallengeSet.setOnClickListener{
            val challengeSet = vm.challengeSet.value
            if(challengeSet!=null){
                vm.deleteChallengeSet(challengeSet)
            }
            finish()
        }
    }

    inner class ChallengeListAdapter(
        private val context: Context,
        private val challenges: List<Challenge>
    ) :
        RecyclerView.Adapter<ChallengeListAdapter.ItemViewHolder>() {
        inner class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            val textViewChallengeText: TextView =
                view.findViewById(R.id.text_view_challenge_list_challenge)
            val textViewRunningNumber: TextView = view.findViewById(R.id.text_view_challenge_number)
        }

        override fun getItemCount(): Int {
            return challenges.size
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val challengeSet = vm.challengeSet.value
            val text = challenges[position].text
            holder.textViewChallengeText.text = text
            holder.textViewRunningNumber.text = "#" + (position + 1).toString()
            holder.view.setOnClickListener {
                if(challengeSet?.isOwnChallengeSet(this@ChallengeSetActivity) == true){
                    val intent = Intent(context, EditChallengeActivity::class.java)
                    intent.putExtra("CHALLENGE_ID", challenges[position].id)
                    context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val adapterLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_challenge, parent, false)
            return ItemViewHolder(
                adapterLayout
            )
        }

    }
}