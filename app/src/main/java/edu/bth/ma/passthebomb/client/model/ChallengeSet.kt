package edu.bth.ma.passthebomb.client.model

data class ChallengeSetOverview (
    val id: String,
    val name: String,
    val downloads: Int
)

data class Challenge (
    val name: String,
    val text: String,
    val timeLimit: Int
)

class ChallengeSet(val id: String, var name: String, var downloads: Int){
    var challenges: ArrayList<Challenge> = ArrayList()

    fun getChallengeTextList(): ArrayList<String>{
        return ArrayList<String>(challenges.map{challenge -> challenge.text})
    }
}