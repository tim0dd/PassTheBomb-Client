package edu.bth.ma.passthebomb.client.database

import androidx.lifecycle.LiveData
import androidx.room.*
import edu.bth.ma.passthebomb.client.database.DbConstants.CHALLENGESETOVERVIEW_COLUMN
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview
import edu.bth.ma.passthebomb.client.model.ChallengeSet


@Dao
interface ChallengeSetDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addChallengeSetOverview(challengeSetOverview: ChallengeSetOverview)

    @Update
    fun updateChallengeSetOverview(challengeSetOverview: ChallengeSetOverview)

    @Delete
    fun deleteChallengeSetOverview(challengeSetOverview: ChallengeSetOverview)

    @Query("DELETE FROM $CHALLENGESETOVERVIEW_COLUMN WHERE id = :id")
    fun deleteChallengeSetOverview(id: Int)

    @Query("DELETE FROM $CHALLENGESETOVERVIEW_COLUMN")
    fun deleteAllChallengeSetOverviews()

    @Query("SELECT * FROM $CHALLENGESETOVERVIEW_COLUMN ORDER BY addedDate ASC")
    fun getAllChallengeSetOverviews(): LiveData<List<ChallengeSetOverview>>

    @Query("SELECT * FROM $CHALLENGESETOVERVIEW_COLUMN WHERE id =:id")
    fun getChallengeSetOverview(id: Int): LiveData<ChallengeSetOverview>

    //has to be annotated with transaction since it's querying two tables
    @Transaction
    @Query("SELECT * FROM $CHALLENGESETOVERVIEW_COLUMN ORDER BY addedDate ASC")
    fun getAllChallengeSets():  LiveData<List<ChallengeSet>>

    //has to be annotated with transaction since it's querying two tables
    @Transaction
    @Query("SELECT * FROM $CHALLENGESETOVERVIEW_COLUMN  WHERE id =:id")
    fun getChallengeSet(id: Int):  LiveData<ChallengeSet?>

}