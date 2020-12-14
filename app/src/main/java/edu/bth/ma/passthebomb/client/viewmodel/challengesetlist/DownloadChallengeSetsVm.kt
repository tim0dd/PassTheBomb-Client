package edu.bth.ma.passthebomb.client.viewmodel.challengesetlist

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.bth.ma.passthebomb.client.remote.RestService

class DownloadChallengeSetsVm(application: Application) : ChallengeSetListVm(application) {

    override fun init(context: AppCompatActivity){
        val restService = RestService(context)
        restService.getChallengeSetOverviews({
            challengeSetOverviews.value = ArrayList(it)
        },{
            Toast.makeText(context,
                "Could not retrieve the list of challenge sets from the server.",
                Toast.LENGTH_SHORT).show()
        })
    }

    override fun onChallengeSetClick(index: Int, context: Context) {}

    override fun onButton(activity: AppCompatActivity) {}
}