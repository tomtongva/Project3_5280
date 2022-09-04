package com.group3.project2.screens.tasks

enum class GameActionOption(val title: String) {
    EditGame("Edit task"),
    ToggleFlag("Toggle flag"),
    DeleteGame("Delete task");

    companion object {
        fun getByTitle(title: String): GameActionOption {
            values().forEach { action ->
                if (title == action.title) return action
            }

            return EditGame
        }

       fun getOptions(): List<String> {
            val options = mutableListOf<String>()
            values().forEach { taskAction -> options.add(taskAction.title) }
            return options
        }
    }
}
