package com.group3.project2.screens.lobby

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.viewModelScope
import com.group3.project2.*
import com.group3.project2.common.utils.GoogleFunctionsEnabled
import com.group3.project2.model.Game
import com.group3.project2.model.service.AccountService
import com.group3.project2.model.service.FunctionService
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
    private val accountService: AccountService,
    private val functionService: FunctionService
) : UnoViewModel(logService) {
    var games = mutableStateMapOf<String, Game>()
        private set

    fun addListener() {
        viewModelScope.launch(showErrorExceptionHandler) {
            storageService.addListener(::onDocumentEvent, ::onError)
        }
    }

    fun removeListener() {
        viewModelScope.launch(showErrorExceptionHandler) { storageService.removeListener() }
    }

    fun onAddClick(openScreen: (String) -> Unit) = openScreen(NEW_GAME_SCREEN)

    fun onSettingsClick(openScreen: (String) -> Unit) = openScreen(SETTINGS_SCREEN)

    fun onGameClick(openScreen: (String) -> Unit, game: Game) {
        viewModelScope.launch(showErrorExceptionHandler) {
            if(GoogleFunctionsEnabled){
                //TODO: Add in logic for joinGame
                functionService.joinGame("test")
            } else {
                if (accountService.getUserId() != game.hostId) {
                    val editedGame = game.copy(guestId = accountService.getUserId())
                    updateGame(editedGame, openScreen)
                }
            }
        }
    }

    private fun updateGame(game: Game, openScreen: (String) -> Unit) {
        storageService.updateGame(game) { error ->
            if (error == null) {
                openScreen("$GAME_SCREEN?$GAME_ID={${game.hostId}}")
            } else onError(error)
        }
    }


    private fun onDocumentEvent(wasDocumentDeleted: Boolean, game: Game) {
        if (wasDocumentDeleted) games.remove(game.hostId) else games[game.hostId] = game
    }
}