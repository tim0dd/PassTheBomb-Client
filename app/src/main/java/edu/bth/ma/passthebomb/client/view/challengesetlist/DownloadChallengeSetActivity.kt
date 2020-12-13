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
import androidx.lifecycle.observe
import edu.bth.ma.passthebomb.client.R
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview
import edu.bth.ma.passthebomb.client.remote.RestService

import edu.bth.ma.passthebomb.client.viewmodel.challengesetlist.ChallengeSetListVm
import edu.bth.ma.passthebomb.client.viewmodel.challengesetlist.DownloadChallengeSetsVm

class DownloadChallengeSetActivity : ChallengeSetListActivity() {

    override val vm: ChallengeSetListVm by viewModels<DownloadChallengeSetsVm>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Download Challenge Sets"

        val restService = RestService(this)
        restService.getChallengeSetOverviews({
            val adapter = DownloadChallengeSetsAdapter(this, it)
            this.challengeSetsAdapter = adapter
        },{
            Toast.makeText(this,
                "Could not retrieve the list of challenge sets from the server.",
                Toast.LENGTH_SHORT).show()
        })
    }


    override fun initButton(){
        val addButton = findViewById<Button>(R.id.button_add_challenge_set)
        addButton.visibility = View.GONE
    }

    inner class DownloadChallengeSetsAdapter(private val context: Context,
                                       private val dataset: List<ChallengeSetOverview>
    ) : ChallengeSetsAdapter(context, dataset){

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val challengeSet = dataset[position]
            holder.textViewChallengeSetName.text =  challengeSet.name
            val imageView = holder.view.findViewById<ImageView>(R.id.image_view_item_online_challenge_set)
            val progressBar = holder.view.findViewById<ProgressBar>(R.id.progress_bar_challenge_set_download)
            vm.getChallengeSet(dataset[position].id).observe(this@DownloadChallengeSetActivity,
            Observer {
                progressBar.visibility = View.GONE
                if(it==null){
                    imageView.setImageResource(R.drawable.ic_download)
                }else{
                    imageView.setImageResource(R.drawable.ic_check)
                }
            })
            holder.view.setOnClickListener{
                progressBar.visibility = View.VISIBLE
                val restService = RestService(this@DownloadChallengeSetActivity)
                val challengSet = dataset[position]
                restService.getChallengeSet(challengSet.id,{
                    vm.addChallengeSet(it)
                },{
                    Toast.makeText(this@DownloadChallengeSetActivity,
                        "Could not download challenge set with name ${challengSet.name}.",
                        Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                })
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val adapterLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_online_challenge_set, parent, false)
            return ItemViewHolder(adapterLayout)
        }
    }
}