package edu.bth.ma.passthebomb.client.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview

class DownloadChallengeSetsAdapter(private val context: Context,
                                   private val dataset: List<ChallengeSetOverview>
) : ChallengeSetsAdapter(context, dataset){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_online_challenge_set, parent, false)
        return ItemViewHolder(adapterLayout)
    }
}