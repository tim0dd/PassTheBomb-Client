package edu.bth.ma.passthebomb.client.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import edu.bth.ma.passthebomb.client.database.DbConstants
import java.util.*

@JsonClass(generateAdapter = true)
@Entity(tableName = DbConstants.CHALLENGESETOVERVIEW_COLUMN)
data class ChallengeSetOverview(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    var creatorId: String,
    var name: String,
    val createdDate: Date,
    var modifiedDate: Date,
    //uploadedDate will be null when there hasn't been an upload yet
    var uploadedDate: Date? = null,
    //addedDate will be null when data is coming from server
    var addedDate: Date? = null,
    var downloads: Int? = 0
)