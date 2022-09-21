package com.group3.project2.screens.new_game

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.group3.project2.*
import com.group3.project2.common.utils.GoogleFunctionsEnabled
import com.group3.project2.model.Card
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
class NewGameViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val accountService: AccountService,
    private val functionService: FunctionService
) : UnoViewModel(logService) {
    var game = mutableStateOf(Game())
        private set

    fun onTitleChange(newValue: String) {
        game.value = game.value.copy(title = newValue)
    }

    private fun createGame() {
        // Create card deck
        var cards: MutableList<Card> = mutableListOf<Card>()

        // Create cards of four different colors and 0-9, S, +4
        val colors = arrayListOf("red", "green", "blue", "yellow")

        for (color in colors) {
            for (i in 0..11) {
                var editedColor = color
                var content = i.toString()
                if (i == 10) {
                    content = "S"
                } else if (i == 11) {
                    content = "+4"
                    editedColor = ""
                }
                cards.add(Card(id = content + color, content = content, color = editedColor))
            }
        }

        // Shuffle cards
        cards.shuffle()

        // Deal 7 cards to each player
        var hostHand: MutableList<Card> = mutableListOf<Card>()
        var guestHand: MutableList<Card> = mutableListOf<Card>()

        for (i in 1..7) {
            hostHand.add(cards.removeLast())
            guestHand.add(cards.removeLast())
        }

        // Sort hands by color then content
        hostHand.sortWith(compareBy({ it.color }, { it.content }))
        guestHand.sortWith(compareBy({ it.color }, { it.content }))

        var discardPile: MutableList<Card> = mutableListOf<Card>()

        // Does not allow game to start with +4 and adds card from deck to discard game
        while(cards.last().content == "+4") {
            cards.shuffle()
        }
        discardPile.add(cards.removeLast())

        game.value = game.value.copy(cards = cards, hostHand = hostHand, guestHand = guestHand, discardPile = discardPile)
    }

    fun onDoneClick(openAndPopUp: (String, String) -> Unit) {
        viewModelScope.launch(showErrorExceptionHandler) {
            if(GoogleFunctionsEnabled) {
                //TODO: Add in logic for createNewGame
                functionService.createNewGame(game.value.title)
                    .addOnCompleteListener { result ->
                        result.result
                        openAndPopUp("$GAME_SCREEN?$GAME_ID={${accountService.getUserId()}}", NEW_GAME_SCREEN)
                    }
            } else {
                createGame()
                val editedGame = game.value.copy(hostId = accountService.getUserId())
                saveGame(editedGame, openAndPopUp)
            }
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
