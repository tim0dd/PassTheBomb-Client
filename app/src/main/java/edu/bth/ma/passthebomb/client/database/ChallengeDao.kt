package edu.bth.ma.passthebomb.client.database

import androidx.lifecycle.LiveData
import androidx.room.*
import edu.bth.ma.passthebomb.client.database.DbConstants.CHALLENGE_COLUMN
import edu.bth.ma.passthebomb.client.model.Challenge


@Dao
interface ChallengeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addChallenge(challenge: Challenge)

    @Update
    fun updateChallenge(challenge: Challenge)

    @Delete
    fun deleteChallenge(challenge: Challenge)

    @Query("DELETE FROM $CHALLENGE_COLUMN WHERE id = :id")
    fun deleteChallenge(id: String)

    @Query("DELETE FROM $CHALLENGE_COLUMN")
    fun deleteAllChallenges()

    @Query("SELECT * FROM $CHALLENGE_COLUMN ORDER BY createdDate ASC")
    fun getAllChallenges(): LiveData<List<Challenge>>

    @Query("SELECT * FROM $CHALLENGE_COLUMN WHERE challengeSetId =:overviewId")
    fun getChallengesByOverviewId(overviewId: String): LiveData<List<Challenge>>

    @Query("SELECT * FROM $CHALLENGE_COLUMN WHERE id =:id")
    fun getChallenge(id: String): LiveData<Challenge?>

    @Query("SELECT * FROM $CHALLENGE_COLUMN where challengeSetId In (:idList)")
    fun getChallengesByOverviewIds(idList: List<String>): LiveData<List<Challenge>>
}