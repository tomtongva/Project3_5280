package com.group3.project2.screens

import androidx.lifecycle.ViewModel
import com.group3.project2.common.snackbar.SnackbarManager
import com.group3.project2.common.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.group3.project2.model.service.LogService
import kotlinx.coroutines.CoroutineExceptionHandler

open class UnoViewModel(private val logService: LogService) : ViewModel() {
    open val showErrorExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError(throwable)
    }

    open val logErrorExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        logService.logNonFatalCrash(throwable)
    }

    open fun onError(error: Throwable) {
        SnackbarManager.showMessage(error.toSnackbarMessage())
        logService.logNonFatalCrash(error)
    }
}