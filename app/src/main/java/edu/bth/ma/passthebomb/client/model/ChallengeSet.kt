package edu.bth.ma.passthebomb.client.model

import android.content.Context
import androidx.room.Embedded
import androidx.room.Relation

data class ChallengeSet(
    @Embedded val challengeSetOverview: ChallengeSetOverview,
    @Relation(
        parentColumn = "id",
        entityColumn = "challengeSetId"
    )
    var challenges: List<Challenge>
){
    fun isOwnChallengeSet(context: Context):Boolean{
        return challengeSetOverview.isOwnChallengeSet(context)
    }
}