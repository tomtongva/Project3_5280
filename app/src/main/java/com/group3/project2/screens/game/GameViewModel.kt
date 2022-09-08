package com.group3.project2.screens.game

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.group3.project2.R
import com.group3.project2.common.ext.idFromParameter
import com.group3.project2.common.snackbar.SnackbarManager
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

    fun addListener() {
        viewModelScope.launch(showErrorExceptionHandler) {
            storageService.addListener(::onDocumentEvent, ::onError)
        }
    }

    fun removeListener() {
        viewModelScope.launch(showErrorExceptionHandler) { storageService.removeListener() }
    }

    fun onCardClick(card: Card, hostHand: Boolean) {
        viewModelScope.launch(showErrorExceptionHandler) {
            var editedGame = game.value.copy()

            if (hostHand && game.value.hostsMove) {
                if (card.content == "+4") {
                    editedGame.hostHand.remove(card)
                    var editedCard = card
                    editedCard.color = "red"
                    editedGame.discardPile.add(0, editedCard)

                    for(i in 0..3) {
                        editedGame.guestHand.add(0, editedGame.cards.removeLast())
                    }

                    editedGame.hostsMove = false
                } else if (card.content == game.value.discardPile[0].content || card.color == game.value.discardPile[0].color) {
                    editedGame.hostHand.remove(card)
                    editedGame.discardPile.add(0, card)
                    editedGame.nextMove = !editedGame.nextMove

                    if (card.content != "S") {
                        editedGame.hostsMove = false
                    }
                } else {
                    SnackbarManager.showMessage(R.string.invalidMove)
                }
            } else if (!hostHand && !game.value.hostsMove) {
                if (card.content == "+4") {
                    editedGame.guestHand.remove(card)
                    var editedCard = card
                    editedCard.color = "red"
                    editedGame.discardPile.add(0, editedCard)

                    for(i in 0..3) {
                        editedGame.hostHand.add(0, editedGame.cards.removeLast())
                    }

                    editedGame.hostsMove = true
                } else if (card.content == game.value.discardPile[0].content || card.color == game.value.discardPile[0].color) {
                    editedGame.guestHand.remove(card)
                    editedGame.discardPile.add(0, card)
                    editedGame.nextMove = !editedGame.nextMove

                    if (card.content != "S") {
                        editedGame.hostsMove = true
                    }
                } else {
                    SnackbarManager.showMessage(R.string.invalidMove)
                }
            } else {
                SnackbarManager.showMessage(R.string.wrongTurn)
            }

            saveGame(editedGame)
        }
    }

    fun onDrawCardClick(hostHand: Boolean) {
        viewModelScope.launch(showErrorExceptionHandler) {
            var editedGame = game.value.copy()
            val drawnCard = editedGame.cards.removeLast()

            if (drawnCard.content == game.value.discardPile[0].content || drawnCard.color == game.value.discardPile[0].color) {
                editedGame.discardPile.add(0, drawnCard)
            } else if (hostHand && game.value.hostsMove) {
                editedGame.hostHand.add(0, drawnCard)
            } else if (!hostHand && !game.value.hostsMove) {
                editedGame.guestHand.add(0, drawnCard)
            }

            editedGame.nextMove = !editedGame.nextMove

            if (drawnCard.content != "S") {
                editedGame.hostsMove = !editedGame.hostsMove
            }
            saveGame(editedGame)
        }
    }

    fun onExitClick(popUpScreen: () -> Unit) {
        viewModelScope.launch(showErrorExceptionHandler) {
            popUpScreen()
        }
    }

    private fun saveGame(game: Game) {
        storageService.saveGame(game) { error ->
            if (error == null) else onError(error)
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
