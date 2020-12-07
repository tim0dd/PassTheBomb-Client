package edu.bth.ma.passthebomb.client.model

import androidx.room.Embedded
import androidx.room.Relation

data class ChallengeSet(
    @Embedded val challengeSetOverview: ChallengeSetOverview,
    @Relation(
        parentColumn = "id",
        entityColumn = "challengeSetId"
    )
    var challenges: List<Challenge>
)