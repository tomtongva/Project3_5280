package com.group3.project2.screens.game

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.group3.project2.*
import com.group3.project2.common.ext.idFromParameter
import com.group3.project2.model.Card
import com.group3.project2.model.Game
import com.group3.project2.model.service.AccountService
import com.group3.project2.model.service.LogService
import com.group3.project2.model.service.StorageService
import com.group3.project2.screens.UnoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val accountService: AccountService
) : UnoViewModel(logService) {
    var game = mutableStateOf(Game())
        private set
    val currentUser = accountService.getUserId()

    fun initialize(gameId: String) {
        viewModelScope.launch(showErrorExceptionHandler) {
            storageService.getGame(gameId.idFromParameter(), ::onError) {
                game.value = it
            }
        }
    }

    fun onDoneClick(popUpScreen: () -> Unit) {
        viewModelScope.launch(showErrorExceptionHandler) {
            popUpScreen()
        }
    }

    private fun saveGame(game: Game, popUpScreen: () -> Unit) {
        storageService.saveGame(game) { error ->
            if (error == null) popUpScreen() else onError(error)
        }
    }
    private fun updateGame(game: Game) {
        storageService.updateGame(game) { error ->
            if (error == null) else onError(error)
        }
    }

}
