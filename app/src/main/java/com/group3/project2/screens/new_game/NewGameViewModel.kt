package com.group3.project2.screens.new_game

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.group3.project2.GAME_DEFAULT_ID
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
class NewGameViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val accountService: AccountService
) : UnoViewModel(logService) {
    var game = mutableStateOf(Game())
        private set

    fun onTitleChange(newValue: String) {
        game.value = game.value.copy(title = newValue)
    }

    private fun createDeck() {
        var cards: MutableList<Card> = mutableListOf<Card>()

        val colors = arrayListOf("red", "green", "blue", "yellow")

        for (color in colors) {
            for (i in 0..11) {
                var content = i.toString()
                if (i == 10) {
                    content = "S"
                } else if (i == 11) {
                    content = "+4"
                }
                cards.add(Card(id = content + color, content = content, color = color))
            }
        }
        cards.shuffle()
        game.value = game.value.copy(cards = cards)
    }

    fun onDoneClick(popUpScreen: () -> Unit) {
        viewModelScope.launch(showErrorExceptionHandler) {
            createDeck()
            val editedGame = game.value.copy(hostId = accountService.getUserId())
            saveGame(editedGame, popUpScreen)
        }
    }

    private fun saveGame(game: Game, popUpScreen: () -> Unit) {
        storageService.saveGame(game) { error ->
            if (error == null) popUpScreen() else onError(error)
        }
    }
}
