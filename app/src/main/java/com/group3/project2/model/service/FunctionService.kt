package com.group3.project2.model.service

import com.google.android.gms.tasks.Task

interface FunctionService {
    fun createNewGame(text: String) : Task<HashMap<*, *>>
    fun joinGame(text: String) : Task<String>
    fun drawCard(text: String) : Task<String>
    fun playCard(text: String) : Task<String>
    fun leaveGame(text: String) : Task<String>
}