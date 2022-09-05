package com.group3.project2.screens.game

import com.group3.project2.screens.login.LoginUiState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.group3.project2.LOGIN_SCREEN
import com.group3.project2.SIGN_UP_SCREEN
import com.group3.project2.LOBBY_SCREEN
import com.group3.project2.R.string as AppText
import com.group3.project2.common.ext.isValidEmail
import com.group3.project2.common.snackbar.SnackbarManager
import com.group3.project2.model.service.AccountService
import com.group3.project2.model.service.LogService
import com.group3.project2.model.service.StorageService
import com.group3.project2.screens.UnoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val accountService: AccountService,
    private val storageService: StorageService,
    private val logService: LogService
) : UnoViewModel(logService) {
    var uiState = mutableStateOf(LoginUiState())
        private set

    private val email get() = uiState.value.email
    private val password get() = uiState.value.password

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        if (password.isBlank()) {
            SnackbarManager.showMessage(AppText.empty_password_error)
            return
        }

        viewModelScope.launch(showErrorExceptionHandler) {
            val oldUserId = accountService.getUserId()
            accountService.authenticate(email, password) { error ->
                if (error == null) {
                    linkWithEmail()
                    updateUserId(oldUserId, openAndPopUp)
                } else onError(error)
            }
        }
    }

    fun onSignUpClick(openScreen: (String) -> Unit) = openScreen(SIGN_UP_SCREEN)

    private fun linkWithEmail() {
        viewModelScope.launch(showErrorExceptionHandler) {
            accountService.linkAccount(email, password) { error ->
                if (error != null) logService.logNonFatalCrash(error)
            }
        }
    }

    private fun updateUserId(oldUserId: String, openAndPopUp: (String, String) -> Unit) {
        viewModelScope.launch(showErrorExceptionHandler) {
            val newUserId = accountService.getUserId()

            storageService.updateUserId(oldUserId, newUserId) { error ->
                if (error != null) logService.logNonFatalCrash(error)
                else openAndPopUp(LOBBY_SCREEN, LOGIN_SCREEN)
            }
        }
    }

    fun onForgotPasswordClick() {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        viewModelScope.launch(showErrorExceptionHandler) {
            accountService.sendRecoveryEmail(email) { error ->
                if (error != null) onError(error)
                else SnackbarManager.showMessage(AppText.recovery_email_sent)
            }
        }
    }
}