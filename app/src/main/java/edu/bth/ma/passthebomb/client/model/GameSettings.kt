package edu.bth.ma.passthebomb.client.model

import java.io.Serializable

data class GameSettings(
    val challengeSetIds: List<String>,
    val playerList: List<String>,
    val timeModifier: Double = 1.0,
    val bombSensitivity: Float = 0.5f
): Serializable