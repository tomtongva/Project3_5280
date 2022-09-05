package com.group3.project2.screens.lobby

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.viewModelScope
import com.group3.project2.GAME_SCREEN
import com.group3.project2.NEW_GAME_SCREEN
import com.group3.project2.SETTINGS_SCREEN
import com.group3.project2.TASK_ID
import com.group3.project2.model.Task
import com.group3.project2.model.service.AccountService
import com.group3.project2.model.service.LogService
import com.group3.project2.model.service.StorageService
import com.group3.project2.screens.UnoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LobbyViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val accountService: AccountService
) : UnoViewModel(logService) {
    var tasks = mutableStateMapOf<String, Task>()
        private set

    fun addListener() {
        viewModelScope.launch(showErrorExceptionHandler) {
            storageService.addListener(accountService.getUserId(), ::onDocumentEvent, ::onError)
        }
    }

    fun removeListener() {
        viewModelScope.launch(showErrorExceptionHandler) { storageService.removeListener() }
    }

    fun onTaskCheckChange(task: Task) {
        viewModelScope.launch(showErrorExceptionHandler) {
            val updatedTask = task.copy(completed = !task.completed)

            storageService.updateTask(updatedTask) { error ->
                if (error != null) onError(error)
            }
        }
    }

    fun onAddClick(openScreen: (String) -> Unit) = openScreen(NEW_GAME_SCREEN)

    fun onSettingsClick(openScreen: (String) -> Unit) = openScreen(SETTINGS_SCREEN)

    fun onGameClick(openScreen: (String) -> Unit) = openScreen(GAME_SCREEN)

    fun onTaskActionClick(openScreen: (String) -> Unit, task: Task, action: String) {
        when (GameActionOption.getByTitle(action)) {
            GameActionOption.EditGame -> openScreen("$NEW_GAME_SCREEN?$TASK_ID={${task.id}}")
            GameActionOption.ToggleFlag -> onFlagTaskClick(task)
            GameActionOption.DeleteGame -> onDeleteTaskClick(task)
        }
    }

    private fun onFlagTaskClick(task: Task) {
        viewModelScope.launch(showErrorExceptionHandler) {
            val updatedTask = task.copy(flag = !task.flag)

            storageService.updateTask(updatedTask) { error ->
                if (error != null) onError(error)
            }
        }
    }

    private fun onDeleteTaskClick(task: Task) {
        viewModelScope.launch(showErrorExceptionHandler) {
            storageService.deleteTask(task.id) { error ->
                if (error != null) onError(error)
            }
        }
    }

    private fun onDocumentEvent(wasDocumentDeleted: Boolean, task: Task) {
        if (wasDocumentDeleted) tasks.remove(task.id) else tasks[task.id] = task
    }
}