package edu.bth.ma.passthebomb.client.view.challengesetlist

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
        setContentView(R.layout.screen_my_challenge_sets)

        recyclerView = findViewById<RecyclerView>(R.id.recycleviewMyChallengeSets)
        recyclerView.adapter = getRecyclerViewAdapter()
        recyclerView.setHasFixedSize(true)

        initButton()

        val button : Button = findViewById<Button>(R.id.button_add_challenge_set)
        button.setOnClickListener{vm.onButton(this)}
    }

    abstract fun initButton()

    abstract fun getRecyclerViewAdapter(): ChallengeSetsAdapter

    abstract inner class ChallengeSetsAdapter(private val context: Context,
                                        private val dataset: List<ChallengeSetOverview>
    ) : RecyclerView.Adapter<ChallengeSetsAdapter.ItemViewHolder>(){
        inner class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
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