package com.group3.project2.screens.edit_task

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.group3.project2.TASK_DEFAULT_ID
import com.group3.project2.common.ext.idFromParameter
import com.group3.project2.model.Task
import com.group3.project2.model.service.AccountService
import com.group3.project2.model.service.LogService
import com.group3.project2.model.service.StorageService
import com.group3.project2.screens.UnoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val accountService: AccountService
) : UnoViewModel(logService) {
    var task = mutableStateOf(Task())
        private set

    fun initialize(taskId: String) {
        viewModelScope.launch(showErrorExceptionHandler) {
            if (taskId != TASK_DEFAULT_ID) {
                storageService.getTask(taskId.idFromParameter(), ::onError) {
                    task.value = it
                }
            }
        }
    }

    fun onTitleChange(newValue: String) {
        task.value = task.value.copy(title = newValue)
    }

    fun onDescriptionChange(newValue: String) {
        task.value = task.value.copy(description = newValue)
    }

    fun onUrlChange(newValue: String) {
        task.value = task.value.copy(url = newValue)
    }

    fun onDateChange(newValue: Long) {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone(UTC))
        calendar.timeInMillis = newValue
        val newDueDate = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(calendar.time)
        task.value = task.value.copy(dueDate = newDueDate)
    }

    fun onTimeChange(hour: Int, minute: Int) {
        val newDueTime = "${hour.toClockPattern()}:${minute.toClockPattern()}"
        task.value = task.value.copy(dueTime = newDueTime)
    }

    fun onFlagToggle(newValue: String)  {
        val newFlagOption = EditFlagOption.getBooleanValue(newValue)
        task.value = task.value.copy(flag = newFlagOption)
    }

    fun onPriorityChange(newValue: String) {
        task.value = task.value.copy(priority = newValue)
    }

    fun onDoneClick(popUpScreen: () -> Unit) {
        viewModelScope.launch(showErrorExceptionHandler) {
            val editedTask = task.value.copy(userId = accountService.getUserId())

            if (editedTask.id.isBlank()) saveTask(editedTask, popUpScreen)
            else updateTask(editedTask, popUpScreen)
        }
    }

    private fun saveTask(task: Task, popUpScreen: () -> Unit) {
        storageService.saveTask(task) { error ->
            if (error == null) popUpScreen() else onError(error)
        }
    }

    private fun updateTask(task: Task, popUpScreen: () -> Unit) {
        storageService.updateTask(task) { error ->
            if (error == null) popUpScreen() else onError(error)
        }
    }

    private fun Int.toClockPattern(): String {
        return if (this < 10) "0$this" else "$this"
    }

    companion object {
        private const val UTC = "UTC"
        private const val DATE_FORMAT = "EEE, d MMM yyyy"
    }
}
