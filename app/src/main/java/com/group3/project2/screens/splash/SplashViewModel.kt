package com.group3.project2.screens.splash

import com.group3.project2.LOGIN_SCREEN
import com.group3.project2.SPLASH_SCREEN
import com.group3.project2.LOBBY_SCREEN
import com.group3.project2.model.service.AccountService
import com.group3.project2.model.service.LogService
import com.group3.project2.screens.UnoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val accountService: AccountService,
    private val logService: LogService
) : UnoViewModel(logService) {
    fun onAppStart(openAndPopUp: (String, String) -> Unit) {
        if (accountService.hasUser()) openAndPopUp(LOBBY_SCREEN, SPLASH_SCREEN)
        else openAndPopUp(LOGIN_SCREEN, SPLASH_SCREEN)
    }
}