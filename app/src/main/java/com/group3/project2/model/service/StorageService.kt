package com.group3.project2.model.service

import com.group3.project2.model.Game

interface StorageService {
    fun addListener(
        onDocumentEvent: (Boolean, Game) -> Unit,
        onError: (Throwable) -> Unit
    )

    fun removeListener()
    fun getGame(gameId: String, onError: (Throwable) -> Unit, onSuccess: (Game) -> Unit)
    fun saveGame(game: Game, onResult: (Throwable?) -> Unit)
    fun updateGame(game: Game, onResult: (Throwable?) -> Unit)
    fun deleteGame(gameId: String, onResult: (Throwable?) -> Unit)
    fun deleteAllForUser(userId: String, onResult: (Throwable?) -> Unit)
    fun updateUserId(oldUserId: String, newUserId: String, onResult: (Throwable?) -> Unit)
}
