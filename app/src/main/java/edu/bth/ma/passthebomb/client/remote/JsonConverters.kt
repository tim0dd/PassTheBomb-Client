package edu.bth.ma.passthebomb.client.remote

import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import edu.bth.ma.passthebomb.client.model.Challenge
import edu.bth.ma.passthebomb.client.model.ChallengeSet
import edu.bth.ma.passthebomb.client.model.ChallengeSetOverview
import org.json.JSONArray
import org.json.JSONObject
import java.util.*


class JsonConverters {

    companion object {
        fun jsonToChallengeList(value: String?): List<Challenge>? {
            val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
            val listType = Types.newParameterizedType(List::class.java, Challenge::class.java)
            val adapter: JsonAdapter<List<Challenge>> = moshi.adapter(listType)
            return value?.let { adapter.fromJson(value) }
        }

        fun challengeListToJson(challengeList: List<Challenge>?): String? {
            val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
            val listType = Types.newParameterizedType(List::class.java, Challenge::class.java)
            val adapter: JsonAdapter<List<Challenge>> = moshi.adapter(listType)
            return challengeList?.let { adapter.toJson(challengeList) }
        }

        fun challengeSetToJson(challengeSet: ChallengeSet): JSONObject {
            val string = getChallengeSetAdapter().toJson(challengeSet)
            return JSONObject(string)
        }

        fun jsonToChallengeSet(json: JSONObject): ChallengeSet? {
            return getChallengeSetAdapter().fromJson(json.toString())
        }

        fun challengeSetOverviewListToJson(challengeSetOverviewList: List<ChallengeSetOverview>): JSONArray {
            val string = getChallengeSetOverviewListAdapter().toJson(challengeSetOverviewList)
            return JSONArray(string)
        }

        fun jsonToChallengeSetOverviewList(json: JSONArray): List<ChallengeSetOverview>? {
            return getChallengeSetOverviewListAdapter().fromJson(json.toString())
        }


        private fun getChallengeSetOverviewListAdapter(): JsonAdapter<List<ChallengeSetOverview>> {
            val moshi = Moshi.Builder()
                .add(DateAdapter())
                .addLast(KotlinJsonAdapterFactory())
                .build()
            val listType =
                Types.newParameterizedType(List::class.java, ChallengeSetOverview::class.java)
            return moshi.adapter(listType)
        }

        private fun getChallengeSetAdapter(): JsonAdapter<ChallengeSet> {
            val moshi = Moshi.Builder()
                .add(DateAdapter())
                .addLast(KotlinJsonAdapterFactory())
                .build()
            return moshi.adapter(ChallengeSet::class.java)
        }

    }

    @Suppress("unused")
    class DateAdapter {
        @FromJson
        fun timeStampToDate(value: String?): Date? {
            return value?.let { Date(it.toLong()) }
        }

        @ToJson
        fun dateToTimestamp(date: Date?): String? {
            return date?.time.toString()
        }
    }
}




