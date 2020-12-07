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
    fun deleteChallenge(id: Int)

    @Query("DELETE FROM $CHALLENGE_COLUMN")
    fun deleteAllChallenges()

    @Query("SELECT * FROM $CHALLENGE_COLUMN ORDER BY createdDate ASC")
    fun getAllChallenges(): LiveData<List<Challenge>>

    @Query("SELECT * FROM $CHALLENGE_COLUMN WHERE challengeSetId =:challengeSetId")
    fun getAllChallenges(challengeSetId: Int): LiveData<List<Challenge>>

    @Query("SELECT * FROM $CHALLENGE_COLUMN WHERE id =:id")
    fun getChallenge(id: Int): LiveData<Challenge?>

    @Query("SELECT * FROM $CHALLENGE_COLUMN where id In (:idList)")
    fun getChallenges(idList: List<Int>): LiveData<List<Challenge>>
}