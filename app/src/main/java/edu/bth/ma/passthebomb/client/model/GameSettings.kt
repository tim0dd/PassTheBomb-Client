package edu.bth.ma.passthebomb.client.model

import java.io.Serializable

data class GameSettings(
    val challengeSetIds: List<String> = ArrayList(),
    var playerList: List<String> = ArrayList(),
    val timeModifier: Double = 1.0,
    val bombSensitivity: Double = 0.5,
    val randomScheduling: Boolean = true,
    val enableSound: Boolean = false,
    val numberRounds: Int = 10
): Serializable