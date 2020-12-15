package edu.bth.ma.passthebomb.client.view.challengesetlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.model.ChallengeSet
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview
import edu.bth.ma.passthebomb.client.remote.RestService
import edu.bth.ma.passthebomb.client.viewmodel.DatabaseVm
import edu.bth.ma.passthebomb.client.viewmodel.challengesetlist.DownloadChallengeSetVm

class DownloadChallengeSetActivity : ChallengeSetListActivity() {

    override val vm by viewModels<DownloadChallengeSetVm>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Download Challenge Sets"
    }


    override fun initButton(){
        val addButton = findViewById<Button>(R.id.button_add_challenge_set)
        addButton.visibility = View.GONE
    }

    override fun onButtonClick(view: View) {}

    override fun onChallengeSetClick(view: View, challengeSets: ArrayList<ChallengeSetOverview>, position: Int) {
        val imageView = view.findViewById<ImageView>(R.id.image_view_item_online_challenge_set)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar_challenge_set_download)
        progressBar.visibility = View.VISIBLE
        imageView.setImageResource(0)
        val restService = RestService(this@DownloadChallengeSetActivity)
        val challengeSet = challengeSets[position]
        restService.getChallengeSet(challengeSet.id,{
            vm.addChallengeSet(it)
            imageView.setImageResource(R.drawable.ic_check)
            progressBar.visibility = View.GONE
        },{
            Toast.makeText(this@DownloadChallengeSetActivity,
                "Could not download challenge set with name ${challengeSet.name}.",
                Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE
        })
    }

    override fun getChallengeSetsAdapter(challengeSets: ArrayList<ChallengeSetOverview>): ChallengeSetsAdapter {
        return DownloadChallengeSetsAdapter(this, challengeSets)
    }

    inner class DownloadChallengeSetsAdapter(
        context: Context,
        override val dataset: ArrayList<ChallengeSetOverview>
    ) : ChallengeSetsAdapter(context, dataset){

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val imageView = holder.view.findViewById<ImageView>(R.id.image_view_item_online_challenge_set)
            vm.getChallengeSet(dataset[position].id).observe(this@DownloadChallengeSetActivity,
            Observer {
                if(it==null){
                    imageView.setImageResource(R.drawable.ic_download)
                }else{
                    imageView.setImageResource(R.drawable.ic_check)
                }
            })
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val adapterLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_online_challenge_set, parent, false)
            return ItemViewHolder(adapterLayout)
        }
    }
}