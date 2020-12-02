package edu.bth.ma.passthebomb.client.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import edu.bth.ma.passthebomb.client.model.Challenge
import java.time.LocalDateTime
import java.util.*

@JsonClass(generateAdapter = true)
@Entity(tableName = DbConstants.CHALLENGESET_COLUMN)
data class ChallengeSetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val creatorId: Int,
    val name: String,
    val createdDate: Date,
    val modifiedDate: Date,
    val addedDate: Date,
    val downloads: Int,
    val challenges: List<Challenge>
)