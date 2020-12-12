package edu.bth.ma.passthebomb.client.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.model.Challenge
import edu.bth.ma.passthebomb.client.utils.IdGenerator
import edu.bth.ma.passthebomb.client.viewmodel.DatabaseVm
import java.util.*

class ChallengeSetActivity : ActionBarActivity() {
    val vm: DatabaseVm by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_challenge_set)

        title = "Challenge Set" //TODO: add challenge set name in title

        val challengeSetIdUnsafe: String? = getIntent().getStringExtra("CHALLENGE_SET_ID")
        lateinit var challengeSetId: String
        if (challengeSetIdUnsafe == null) {
            finish()
            challengeSetId = ""
        } else {
            challengeSetId = challengeSetIdUnsafe
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_challenge_list)

        vm.getChallengeSet(challengeSetId).observe(this, androidx.lifecycle.Observer {
            if(it!=null){
                recyclerView.adapter =
                    ChallengeListAdapter(
                        this,
                        it.challenges
                    )
                recyclerView.setHasFixedSize(true)
            }
        })



        val addChallengeButton:Button = findViewById(R.id.button_add_challenge)
        addChallengeButton.setOnClickListener{
            val intent = Intent(this, EditChallengeActivity::class.java)
            val newChallengeId = IdGenerator().generateDbId()
            val challenge = Challenge(newChallengeId, challengeSetId, Date(), "", 60)
            vm.addChallenge(challenge)
            intent.putExtra("CHALLENGE_ID", newChallengeId)
            this.startActivity(intent)
        }
    }

    inner class ChallengeListAdapter(private val context: Context, val challenges: List<Challenge>) :
        RecyclerView.Adapter<ChallengeListAdapter.ItemViewHolder>() {
        inner class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            val textViewChallengeText: TextView = view.findViewById(R.id.text_view_challenge_list_challenge)
        }

        override fun getItemCount(): Int {
            return challenges.size
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val text = challenges.get(position).text
            holder.textViewChallengeText.text = text
            holder.view.setOnClickListener{
                val intent = Intent(context, EditChallengeActivity::class.java)
                intent.putExtra("CHALLENGE_ID", challenges.get(position).id)
                context.startActivity(intent)
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