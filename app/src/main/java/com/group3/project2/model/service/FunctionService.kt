package com.group3.project2.model.service

import com.group3.project2.model.Card

interface FunctionService {
    fun createNewGame(): String
    fun joinGame(gameId:String): Boolean
    fun drawCard(gameId:String): String
    fun playCard(gameId:String, card:String)
    fun leaveGame(gameId:String)
}