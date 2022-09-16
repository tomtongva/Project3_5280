package com.group3.project2.screens.game

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.group3.project2.GAME_SCREEN
import com.group3.project2.LOBBY_SCREEN
import com.group3.project2.LOGIN_SCREEN
import com.group3.project2.R
import com.group3.project2.common.ext.idFromParameter
import com.group3.project2.common.snackbar.SnackbarManager
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
class GameViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val accountService: AccountService,
    private val functionService: FunctionService

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

    fun addListener() {
        viewModelScope.launch(showErrorExceptionHandler) {
            storageService.addListener(::onDocumentEvent, ::onError)
        }
    }

    fun removeListener() {
        viewModelScope.launch(showErrorExceptionHandler) { storageService.removeListener() }
    }

    fun onCardClick(card: Card, hostHand: Boolean, plusFourColor: String) {
        viewModelScope.launch(showErrorExceptionHandler) {
            var editedGame = game.value.copy()

            val validHostMove = findValidHostHand(hostHand)
            var playingHand: MutableList<Card> = mutableListOf<Card>()
            var opponentHand: MutableList<Card> = mutableListOf<Card>()

            // Sets playing hand to host if true, guest if false
            if (validHostMove != null) {
                if (validHostMove == true) {
                    playingHand = editedGame.hostHand
                    opponentHand = editedGame.guestHand
                } else if (validHostMove == false) {
                    playingHand = editedGame.guestHand
                    opponentHand = editedGame.hostHand
                }

                if (card.content == "+4") {
                    playingHand.remove(card)
                    var editedCard = card
                    editedCard.color = plusFourColor
                    editedGame.discardPile.add(0, editedCard)

                    for(i in 0..3) {
                        opponentHand.add(0, editedGame.cards.removeLast())
                    }

                    editedGame.hostsMove = !editedGame.hostsMove
                    opponentHand.sortWith(compareBy({ it.color }, { it.content }))
                } else if (card.content == game.value.discardPile[0].content || card.color == game.value.discardPile[0].color) {
                    playingHand.remove(card)
                    editedGame.discardPile.add(0, card)
                    editedGame.nextMove = !editedGame.nextMove

                    if (card.content != "S") {
                        editedGame.hostsMove = !editedGame.hostsMove
                    }
                    playingHand.sortWith(compareBy({ it.color }, { it.content }))
                } else {
                    SnackbarManager.showMessage(R.string.invalidMove)
                }

                if (playingHand.isEmpty()) {
                    editedGame.gameOver

                    if (game.value.hostsMove) {
                        editedGame.winner = game.value.hostId
                    } else {
                        editedGame.winner = game.value.guestId
                    }
                }
            } else {
                SnackbarManager.showMessage(R.string.wrongTurn)
            }

            saveGame(editedGame)
        }
    }

    fun onCheckCard(): Card {
        return game.value.cards[game.value.cards.lastIndex]
    }


    fun onDrawCardClick(hostHand: Boolean, plusFourColor: String) {
        viewModelScope.launch(showErrorExceptionHandler) {
            var editedGame = game.value.copy()

            val validHostMove = findValidHostHand(hostHand)
            var playingHand: MutableList<Card> = mutableListOf<Card>()
            var opponentHand: MutableList<Card> = mutableListOf<Card>()

            if (validHostMove != null) {
                if (validHostMove == true) {
                    playingHand = editedGame.hostHand
                    opponentHand = editedGame.guestHand
                } else if (validHostMove == false) {
                    playingHand = editedGame.guestHand
                    opponentHand = editedGame.hostHand
                }

                val drawnCard: Card

                if (editedGame.cards.size == 1) {
                    drawnCard = editedGame.cards[0]
                    val placeholderCard = editedGame.discardPile.removeAt(0)
                    editedGame.cards.addAll(editedGame.discardPile)
                    editedGame.discardPile.removeAll(editedGame.discardPile)
                    editedGame.discardPile.add(placeholderCard)
                    editedGame.cards.shuffle()
                } else {
                    drawnCard = editedGame.cards.removeLast()
                }

                if (drawnCard.content == "+4") {
                    var editedCard = drawnCard
                    editedCard.color = plusFourColor
                    editedGame.discardPile.add(0, editedCard)

                    for(i in 0..3) {
                        if (!editedGame.cards.isEmpty()) {
                            opponentHand.add(0, editedGame.cards.removeLast())
                        }
                    }

                    editedGame.hostsMove = !editedGame.hostsMove
                    opponentHand.sortWith(compareBy({ it.color }, { it.content }))

                    SnackbarManager.showMessage("You drew and played a " + drawnCard.content)
                } else if (drawnCard.content == game.value.discardPile[0].content || drawnCard.color == game.value.discardPile[0].color) {
                    editedGame.discardPile.add(0, drawnCard)
                    if (drawnCard.content != "S") {
                        editedGame.hostsMove = !editedGame.hostsMove
                    }
                    SnackbarManager.showMessage("You drew and played a " + drawnCard.content + " of " + drawnCard.color)
                } else {
                    playingHand.add(0, drawnCard)
                    editedGame.hostsMove = !editedGame.hostsMove
                    playingHand.sortWith(compareBy({ it.color }, { it.content }))
                    SnackbarManager.showMessage("You drew a " + drawnCard.content + " of " + drawnCard.color)
                }

                editedGame.nextMove = !editedGame.nextMove
            } else {
                SnackbarManager.showMessage(R.string.wrongTurn)
            }

            saveGame(editedGame)
        }
    }

    // Returns true for a valid host move, false for a valid guest move, null for invalid move
    private fun findValidHostHand(hostHand: Boolean): Boolean? {
        return if (hostHand && game.value.hostsMove) {
            true
        } else if (!hostHand && !game.value.hostsMove) {
            false
        } else {
            SnackbarManager.showMessage(R.string.wrongTurn)
            null
        }
    }

    fun onExitClick(openAndPopUp: (String, String) -> Unit) {
        viewModelScope.launch(showErrorExceptionHandler) {
            var editedGame = game.value.copy()
            editedGame.gameOver = true
            saveGame(editedGame)
            deleteGame(openAndPopUp, game.value)
        }
    }

    private fun saveGame(game: Game) {
        storageService.saveGame(game) { error ->
            if (error == null) else onError(error)
        }
    }

    private fun deleteGame(openAndPopUp: (String, String) -> Unit, game: Game) {
        storageService.deleteGame(game.hostId) { error ->
            if (error == null) {
                openAndPopUp(LOBBY_SCREEN, GAME_SCREEN)
            } else onError(error)

        }
    }

    private fun onDocumentEvent(wasDocumentDeleted: Boolean, gameNew: Game) {
        if (wasDocumentDeleted) {
            //games.remove(game.hostId)
        } else {
            game.value = gameNew
        }
    }
}
