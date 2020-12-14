package edu.bth.ma.passthebomb.client.viewmodel.challengesetlist

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import edu.bth.ma.passthebomb.client.model.Challenge
import edu.bth.ma.passthebomb.client.model.ChallengeSet
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview
import edu.bth.ma.passthebomb.client.utils.IdGenerator
import edu.bth.ma.passthebomb.client.view.ChallengeSetActivity
import edu.bth.ma.passthebomb.client.view.Dialogs
import java.util.*
import kotlin.collections.ArrayList

class MyChallengeSetsVm(application: Application) : ChallengeSetListVm(application) {

    override fun init(context: AppCompatActivity){
        getAllChallengeSets().observe(context,
            Observer {
                challengeSetOverviews.value = ArrayList(it)
            })
    }

    override fun onChallengeSetClick(index: Int, context: Context){
        if(challengeSetOverviews.value!=null){
            val intent = Intent(getApplication(), ChallengeSetActivity::class.java)
            intent.putExtra("CHALLENGE_SET_ID", challengeSetOverviews.value!![index].id)
            getApplication<Application>().startActivity(intent)
        }
    }

    override fun onButton(activity: AppCompatActivity) {
        val dia = Dialogs(activity)
        dia.showStringInputDialog("Challenge Set Name"){
            if(it != ""){
                val ids = IdGenerator()
                val challengeSetId = ids.generateDbId()
                val date = Date()
                val newChallengeSetOverview = ChallengeSetOverview(challengeSetId,
                    ids.getUserId(getApplication()), it, date, date, null, date, 0)
                val newChallengeSet = ChallengeSet(newChallengeSetOverview, ArrayList<Challenge>())
                addChallengeSet(newChallengeSet)
                val intent = Intent(activity, ChallengeSetActivity::class.java)
                intent.putExtra("CHALLENGE_SET_ID", challengeSetId)
                activity.startActivity(intent)
            }
        }

    }
}
