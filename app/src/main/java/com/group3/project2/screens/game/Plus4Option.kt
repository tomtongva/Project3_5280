package com.group3.project2.screens.game

enum class PlusFourOption(val title: String) {
    Blue("blue"),
    Green("green"),
    Red("red"),
    Yellow("yellow");

    companion object {
        fun getByTitle(title: String): PlusFourOption {
            values().forEach { action ->
                if (title == action.title) return action
            }

            return Blue
        }

        fun getOptions(): List<String> {
            val options = mutableListOf<String>()
            values().forEach { taskAction -> options.add(taskAction.title) }
            return options
        }
    }
}