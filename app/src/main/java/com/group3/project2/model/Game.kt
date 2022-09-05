package com.group3.project2.model

data class Game(
    val id: String = "",
    val title: String = "",
    val host: User,
    val guest: User,
    val createdAt: String = "",
    val started: Boolean = false,
    val completed: Boolean = false
)