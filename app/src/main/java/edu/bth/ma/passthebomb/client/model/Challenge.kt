package edu.bth.ma.passthebomb.client.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Challenge(
    val name: String,
    val text: String,
    val timeLimit: Int
)