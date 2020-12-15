package edu.bth.ma.passthebomb.client.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.bth.ma.passthebomb.client.database.DbConstants.DATABASE_NAME
import edu.bth.ma.passthebomb.client.model.Challenge
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview

@Database(
    entities = [ChallengeSetOverview::class, Challenge::class],
    version = 2,
    exportSchema = false
)

@TypeConverters(DbConverters::class)
abstract class AppDb : RoomDatabase() {

    abstract fun challengeSetDao(): ChallengeSetDao

    abstract fun challengeDao(): ChallengeDao

    fun getChallengeSetRepository(): ChallengeSetRepository {
        return ChallengeSetRepository(challengeSetDao())
    }

    fun getChallengeRepository(): ChallengeRepository {
        return ChallengeRepository(challengeDao())
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDb? = null

        fun getDatabase(context: Context): AppDb {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = databaseBuilder(
                    context.applicationContext,
                    AppDb::class.java,
                    DATABASE_NAME
                ).createFromAsset("db/pass_the_bomb_bundled_db")
                    .fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
