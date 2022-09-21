package com.group3.project2.model.service

import com.google.android.gms.tasks.Task
import com.group3.project2.model.Card

interface FunctionService {
    fun createNewGame(gameTitle: String) : Task<HashMap<*, *>>
    fun joinGame(hostId: String, guestId: String) : Task<HashMap<*,*>>
    fun drawCard(gameId: String, hostHand: Boolean, plusFourColor: String) : Task<HashMap<*, *>>
    fun playCard(card: Card, gameId: String, hostHand: Boolean, plusFourColor: String) : Task<HashMap<*, *>>
    fun leaveGame(gameId: String) : Task<HashMap<*,*>>
}