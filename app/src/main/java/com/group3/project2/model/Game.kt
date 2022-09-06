package com.group3.project2.model

data class Game (
    val title: String = "",
    val hostId: String = "",
    val guestId: String = "",
    val started: Boolean = false,
    val completed: Boolean = false,
    val cards: MutableList<Card> = mutableListOf<Card>(),
    val hostHand: MutableList<Card> = mutableListOf<Card>(),
    val guestHand: MutableList<Card> = mutableListOf<Card>(),
)