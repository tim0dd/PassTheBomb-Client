package edu.bth.ma.passthebomb.client.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.bth.ma.passthebomb.client.database.DbConstants.DATABASE_NAME

@Database(
    entities = [ChallengeSetEntity::class],
    version = 4,
    exportSchema = false
)

//TODO: look into caching
@TypeConverters(Converters::class)
abstract class ChallengeSetDatabase : RoomDatabase() {

    abstract fun challengeSetDao(): ChallengeSetDao

    companion object {
        @Volatile
        private var INSTANCE: ChallengeSetDatabase? = null

        fun getDatabase(context: Context): ChallengeSetDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = databaseBuilder(
                    context.applicationContext,
                    ChallengeSetDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
