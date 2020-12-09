package edu.bth.ma.passthebomb.client.database

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import edu.bth.ma.passthebomb.client.model.Challenge
import java.util.*

class DbConverters {
    @TypeConverter
    fun timeStampToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

  /*  @TypeConverter
    fun jsonToChallengeList(value: String?): List<Challenge>? {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val listType = Types.newParameterizedType(List::class.java, Challenge::class.java)
        val adapter: JsonAdapter<List<Challenge>> = moshi.adapter(listType)
        return value?.let { adapter.fromJson(value) }
    }

    @TypeConverter
    fun challengeListToJson(challengeList: List<Challenge>?): String? {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val listType = Types.newParameterizedType(List::class.java, Challenge::class.java)
        val adapter: JsonAdapter<List<Challenge>> = moshi.adapter(listType)
        return challengeList?.let { adapter.toJson(challengeList) }
    }*/
}
