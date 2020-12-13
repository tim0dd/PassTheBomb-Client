package edu.bth.ma.passthebomb.client.viewmodel.challengesetlist

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview
import edu.bth.ma.passthebomb.client.remote.MockRest
import edu.bth.ma.passthebomb.client.remote.RestService

class DownloadChallengeSetsVm(application: Application) : ChallengeSetListVm(application) {
    val challeneSetOverviews = MutableLiveData<List<ChallengeSetOverview>>()

    fun init(){
        val context = getApplication<Application>()
        if(challeneSetOverviews.value==null){

        }
    }

    override fun onChallengeSetClick(index: Int, context: Context) {
        TODO("Not yet implemented")
    }

    override fun onButton(activity: AppCompatActivity) {}
}