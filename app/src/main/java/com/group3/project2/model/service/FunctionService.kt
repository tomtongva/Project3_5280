package com.group3.project2.model.service

import com.google.android.gms.tasks.Task

interface FunctionService {
    fun createNewGame(gameTitle: String) : Task<HashMap<*, *>>
    fun joinGame(hostId: String, guestId: String) : Task<HashMap<*,*>>
    fun drawCard(gameId: String, hostHand: Boolean, plusFourColor: String) : Task<HashMap<*, *>>
    fun playCard(text: String) : Task<String>
    fun leaveGame(gameId: String) : Task<HashMap<*,*>>
}