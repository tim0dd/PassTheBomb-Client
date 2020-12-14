package edu.bth.ma.passthebomb.client.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import edu.bth.ma.passthebomb.client.database.DbConstants
import kotlinx.android.parcel.Parcelize
import java.util.*

@JsonClass(generateAdapter = true)
@Entity(
    tableName = DbConstants.CHALLENGE_COLUMN
    /*   foreignKeys = [ForeignKey(
           entity = ChallengeSetOverview::class, parentColumns = ["id"],
           childColumns = ["challengeSetId"],
           onDelete = CASCADE
       )]*/
)
@Parcelize
data class Challenge(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val challengeSetId: String,
    val createdDate: Date,
    val text: String,
    val timeLimit: Int
) : Parcelable