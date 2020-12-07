package edu.bth.ma.passthebomb.client.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import edu.bth.ma.passthebomb.client.database.DbConstants
import java.util.*

@JsonClass(generateAdapter = true)
@Entity(tableName = DbConstants.CHALLENGESETOVERVIEW_COLUMN)
data class ChallengeSetOverview(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    var creatorId: Int,
    var name: String,
    val createdDate: Date,
    var modifiedDate: Date,
    var addedDate: Date,
    var downloads: Int
)