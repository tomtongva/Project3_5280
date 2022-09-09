package com.group3.project2.screens.new_game

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
        // Create card deck
        var cards: MutableList<Card> = mutableListOf<Card>()

        val colors = arrayListOf("red", "green", "blue", "yellow")

        for (color in colors) {
            for (i in 0..11) {
                var colorEdit = color
                var content = i.toString()
                if (i == 10) {
                    content = "S"
                } else if (i == 11) {
                    content = "+4"
                    colorEdit = ""
                }
                cards.add(Card(id = content + color, content = content, color = colorEdit))
            }
        }
        cards.shuffle()

        // Deal 7 cards to each player
        var hostHand: MutableList<Card> = mutableListOf<Card>()
        var guestHand: MutableList<Card> = mutableListOf<Card>()

        for (i in 1..7) {
            hostHand.add(cards.removeLast())
            guestHand.add(cards.removeLast())
        }

        var discardPile: MutableList<Card> = mutableListOf<Card>()

        while(cards.last().content == "+4") {
            cards.shuffle()
        }
        discardPile.add(cards.removeLast())

        game.value = game.value.copy(cards = cards, hostHand = hostHand, guestHand = guestHand, discardPile = discardPile)
    }

    fun onDoneClick(openAndPopUp: (String, String) -> Unit) {
        viewModelScope.launch(showErrorExceptionHandler) {
            createDeck()
            val editedGame = game.value.copy(hostId = accountService.getUserId())
            saveGame(editedGame, openAndPopUp)
        }
    }

    private fun saveGame(game: Game, openAndPopUp: (String, String) -> Unit) {
        storageService.saveGame(game) { error ->
            if (error == null) {
                openAndPopUp("$GAME_SCREEN?$GAME_ID={${game.hostId}}", NEW_GAME_SCREEN)
            } else onError(error)
        }
    }
}
