package edu.bth.ma.passthebomb.client.view.challengesetlist

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview
import edu.bth.ma.passthebomb.client.view.ActionBarActivity

import edu.bth.ma.passthebomb.client.viewmodel.challengesetlist.ChallengeSetListVm

abstract class ChallengeSetListActivity : ActionBarActivity() {

    lateinit var recyclerView: RecyclerView
    abstract val vm: ChallengeSetListVm


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_challenge_set_list)
        initButton()
        val button : Button = findViewById<Button>(R.id.button_add_challenge_set)
        button.setOnClickListener{vm.onButton(this)}

        val progressBar = findViewById<ProgressBar>(R.id.progress_bar_challenge_set_list)

        vm.init(this)
        vm.challengeSetOverviews.observe(this, Observer {
            recyclerView = findViewById<RecyclerView>(R.id.recycleviewMyChallengeSets)
            recyclerView.adapter = createChallengeSetsAdapter(it)
            recyclerView.setHasFixedSize(true)
            progressBar.visibility = View.GONE
        })
    }

    abstract fun initButton()

    abstract fun createChallengeSetsAdapter(challengeSetOverviews: List<ChallengeSetOverview>): ChallengeSetsAdapter

    abstract inner class ChallengeSetsAdapter(private val context: Context,
                                        private val dataset: List<ChallengeSetOverview>
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
                vm.onChallengeSetClick(position, this@ChallengeSetListActivity)
            }
        }
    }
}