package edu.bth.ma.passthebomb.client.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview

class ChallengeSetAdapter(private val context: Context,
                          private val dataset: List<ChallengeSetOverview>
) : RecyclerView.Adapter<ChallengeSetAdapter.ItemViewHolder>(){
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textViewChallengeSetName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_challenge_set, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val challengeSetOverview = dataset[position]
        holder.textView.text =  challengeSetOverview.name
    }

}