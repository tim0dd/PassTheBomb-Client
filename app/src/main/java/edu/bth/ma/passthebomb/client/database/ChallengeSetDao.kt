package edu.bth.ma.passthebomb.client.database

import androidx.lifecycle.LiveData
import androidx.room.*
import edu.bth.ma.passthebomb.client.database.DbConstants.CHALLENGESET_COLUMN


@Dao
interface ChallengeSetDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addChallengeSet(challengeSetEntity: ChallengeSetEntity)

    @Update
    fun updateChallengeSet(challengeSetEntity: ChallengeSetEntity)

    @Delete
    fun deleteChallengeSet(challengeSetEntity: ChallengeSetEntity)

    @Query("DELETE FROM $CHALLENGESET_COLUMN WHERE id = :id")
    fun deleteChallengeSet(id: Int)

    @Query("DELETE FROM $CHALLENGESET_COLUMN")
    fun deleteAllChallengeSets()

    @Query("SELECT * FROM $CHALLENGESET_COLUMN ORDER BY addedDate ASC")
    fun getChallengeSets(): LiveData<List<ChallengeSetEntity>>

    @Query("SELECT * FROM $CHALLENGESET_COLUMN WHERE id =:id")
    fun getChallengeSet(id: Int): LiveData<ChallengeSetEntity>

}