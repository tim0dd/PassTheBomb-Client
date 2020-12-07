package edu.bth.ma.passthebomb.client.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.viewmodel.ChallengeSetVm
import kotlin.streams.toList

class ChallengeSetActivity : AppCompatActivity() {
    val vm: ChallengeSetVm by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_challenge_set)

        //TODO: refactor to Int
        val challengeSetIdUnsafe: String? = getIntent().getStringExtra("CHALLENGE_SET_ID")
        lateinit var challengeSetId: String
        if (challengeSetIdUnsafe == null) {
            finish()
            challengeSetId = ""
        } else {
            challengeSetId = challengeSetIdUnsafe
        }
        vm.initChallengeSet(challengeSetId)

        val challengeTexts = vm.challengeSet.challenges.stream().map { c -> c.text }.toList()

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_challenge_list)
        recyclerView.adapter =
            ChallengeListAdapter(
                this,
                challengeTexts
            )
        recyclerView.setHasFixedSize(true)

        val addChallengeButton:Button = findViewById(R.id.button_add_challenge)
        addChallengeButton.setOnClickListener{
            vm.onCreateNewChallenge(this)
        }
    }

    inner class ChallengeListAdapter(private val context: Context, val challengeTexts: List<String>) :
        RecyclerView.Adapter<ChallengeListAdapter.ItemViewHolder>() {
        inner class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            val textViewChallengeText: TextView = view.findViewById(R.id.text_view_challenge_list_challenge)
        }

        override fun getItemCount(): Int {
            return challengeTexts.size
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val text = challengeTexts.get(position)
            holder.textViewChallengeText.text = text
            holder.view.setOnClickListener{
                vm.selectChallenge(position, this@ChallengeSetActivity)
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