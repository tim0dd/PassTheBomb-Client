package edu.bth.ma.passthebomb.client.view.challengesetlist

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.model.ChallengeSet
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview
import edu.bth.ma.passthebomb.client.view.ActionBarActivity

import edu.bth.ma.passthebomb.client.viewmodel.challengesetlist.ChallengeSetListVm

abstract class ChallengeSetListActivity : ActionBarActivity() {

    override val vm by viewModels<ChallengeSetListVm>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_challenge_set_list)
        initButton()
        val button : Button = findViewById<Button>(R.id.button_add_challenge_set)
        button.setOnClickListener{onButtonClick(it)}

        val progressBar = findViewById<ProgressBar>(R.id.progress_bar_challenge_set_list)
        val recyclerView = findViewById<RecyclerView>(R.id.recycleviewMyChallengeSets)
        vm.challengeSetOverviews.observe(this, Observer {
            recyclerView.adapter = getChallengeSetsAdapter(it)
            recyclerView.setHasFixedSize(true)
            progressBar.visibility = View.GONE
        })
    }

    abstract fun initButton()

    abstract fun onButtonClick(view: View)

    abstract fun onChallengeSetClick(view: View, challengeSets: ArrayList<ChallengeSetOverview>, position: Int)

    abstract fun getChallengeSetsAdapter(challengeSets: ArrayList<ChallengeSetOverview>): ChallengeSetsAdapter

    abstract inner class ChallengeSetsAdapter(private val context: Context,
                                        protected open val dataset: ArrayList<ChallengeSetOverview>
    ) : RecyclerView.Adapter<ChallengeSetsAdapter.ItemViewHolder>(){
        inner class ItemViewHolder(public val view: View) : RecyclerView.ViewHolder(view) {
            val textViewChallengeSetName: TextView = view.findViewWithTag("text_view_challenge_set_name")
        }

        override fun getItemCount(): Int {
            return dataset.size
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val challengeSet = dataset[position]
            holder.textViewChallengeSetName.text =  challengeSet.name
            holder.view.setOnClickListener{
                onChallengeSetClick(it, dataset, position)
            }
        }
    }
}