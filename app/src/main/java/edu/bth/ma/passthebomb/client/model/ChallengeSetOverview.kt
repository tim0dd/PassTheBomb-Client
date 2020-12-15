package edu.bth.ma.passthebomb.client.model

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import edu.bth.ma.passthebomb.client.database.DbConstants
import edu.bth.ma.passthebomb.client.preferences.PreferenceService
import edu.bth.ma.passthebomb.client.utils.IdGenerator
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
){
    fun isOwnChallengeSet(context: Context): Boolean{
        val personalId = PreferenceService.getInstance(context).getUniqueUserId()
        return personalId==creatorId
    }

    fun isBundledChallengeSet(): Boolean {
        return creatorId=="BUNDLED"
    }

    fun isDownloadedChallengeSet(context: Context): Boolean {
        return !isBundledChallengeSet() && !isOwnChallengeSet(context)
    }

    companion object{
        fun generateNewFromContext(context: Context, name: String): ChallengeSetOverview{
            val ids = IdGenerator()
            val challengeSetId = ids.generateDbId()
            val date = Date()
            return ChallengeSetOverview(challengeSetId,
                ids.getUserId(context), name, date, date, null, date, 0)
        }
    }
}