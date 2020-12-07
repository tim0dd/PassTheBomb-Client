package edu.bth.ma.passthebomb.client.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import edu.bth.ma.passthebomb.client.database.DbConstants
import java.util.*

@JsonClass(generateAdapter = true)
@Entity(
    tableName = DbConstants.CHALLENGE_COLUMN,
    foreignKeys = [ForeignKey(
        entity = ChallengeSetOverview::class, parentColumns = ["id"],
        childColumns = ["challengeSetId"],
        onDelete = CASCADE
    )]
)

data class Challenge(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val challengeSetId: Int?,
    val createdDate: Date,
    val text: String,
    val timeLimit: Int
)