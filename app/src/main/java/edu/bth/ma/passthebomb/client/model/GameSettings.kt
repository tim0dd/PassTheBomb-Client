package edu.bth.ma.passthebomb.client.model

import java.io.Serializable

data class GameSettings(
    val challengeSetIds: List<String>,
    var playerList: List<String>,
    val timeModifier: Double = 1.0,
    val bombSensitivity: Double = 0.5
): Serializable