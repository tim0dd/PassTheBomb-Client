package edu.bth.ma.passthebomb.client.view.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview

abstract class ChallengeSetsAdapter(private val context: Context,
                                    private val dataset: List<ChallengeSetOverview>
) : RecyclerView.Adapter<ChallengeSetsAdapter.ItemViewHolder>(){
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textViewChallengeSetName: TextView = view.findViewWithTag("text_view_challenge_set_name")
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val challengeSetOverview = dataset[position]
        holder.textViewChallengeSetName.text =  challengeSetOverview.name
    }

}