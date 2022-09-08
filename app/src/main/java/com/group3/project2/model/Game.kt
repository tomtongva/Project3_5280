package com.group3.project2.model

data class Game (
    val title: String = "",
    val hostId: String = "",
    val guestId: String = "",
    val gameStarted: Boolean = false,
    val gameOver: Boolean = false,
    var hostsMove: Boolean = true,
    var nextMove: Boolean = false,
    val cards: MutableList<Card> = mutableListOf<Card>(),
    val discardPile: MutableList<Card> = mutableListOf<Card>(),
    val hostHand: MutableList<Card> = mutableListOf<Card>(),
    val guestHand: MutableList<Card> = mutableListOf<Card>(),
)