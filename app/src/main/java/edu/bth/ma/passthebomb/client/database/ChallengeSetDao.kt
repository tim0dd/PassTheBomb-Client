package edu.bth.ma.passthebomb.client.database

import androidx.lifecycle.LiveData
import androidx.room.*
import edu.bth.ma.passthebomb.client.database.DbConstants.CHALLENGESET_COLUMN


@Dao
interface ChallengeSetDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addChallengeSet(challengeSetEntity: ChallengeSetEntity)

    @Transaction
    @Query("SELECT * FROM $CHALLENGESET_COLUMN ORDER BY addedDate ASC")
    fun getChallenges(): LiveData<List<ChallengeSetEntity>>

}