package edu.bth.ma.passthebomb.client.view.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.viewmodel.ChallengeSetVm

class ChallengeSetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge_set)

        val challengeSetIdUnsafe: String? = getIntent().getStringExtra("EXTRA_SESSION_ID")
        lateinit var challengeSetId: String
        if (challengeSetIdUnsafe == null) {
            finish()
            challengeSetId = ""
        } else {
            challengeSetId = challengeSetIdUnsafe
        }
        val vm: ChallengeSetVm by viewModels()
        vm.initChallengeSet(challengeSetId)

        val challengeTexts = vm.challengeSet.getChallengeTextList()

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_challenge_list)
        recyclerView.adapter = ChallengeListAdapter(this, challengeTexts)
        recyclerView.setHasFixedSize(true)
    }

    class ChallengeListAdapter(private val context: Context, val challengeTexts: List<String>) :
        RecyclerView.Adapter<ChallengeListAdapter.ItemViewHolder>() {
        class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            val textViewChallengeText: TextView = view.findViewById(R.id.text_view_player_name)
        }

        override fun getItemCount(): Int {
            return challengeTexts.size
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val text = challengeTexts.get(position)
            holder.textViewChallengeText.text = text
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val adapterLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_challenge, parent, false)
            return ItemViewHolder(adapterLayout)
        }

    }
}