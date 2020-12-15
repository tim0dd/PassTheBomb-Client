package edu.bth.ma.passthebomb.client.viewmodel.challengesetlist

import android.app.Application
import android.widget.Toast
import edu.bth.ma.passthebomb.client.remote.RestService
import kotlin.collections.ArrayList

class DownloadChallengeSetVm(application: Application) : ChallengeSetListVm(application) {

    init{
        scheduleEvent {context ->
            val restService = RestService(context)
            restService.getChallengeSetOverviews({
                challengeSetOverviews.value = ArrayList(it)
            },{
                Toast.makeText(context,
                    "Could not retrieve the list of challenge sets from the server.",
                    Toast.LENGTH_SHORT).show()
            })
        }
    }
}
