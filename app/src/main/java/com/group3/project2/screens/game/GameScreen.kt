package com.group3.project2.screens.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.group3.project2.R
import com.group3.project2.common.composable.*
import com.group3.project2.common.ext.*
import com.group3.project2.model.Card
import com.group3.project2.model.Game
import com.group3.project2.theme.*
import com.group3.project2.R.string as AppText

@ExperimentalMaterialApi
@Composable
fun GameScreen(
    popUpScreen: () -> Unit,
    gameId: String,
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = hiltViewModel()
) {
    val game by viewModel.game
    val currentUserIsHost: Boolean = viewModel.currentUser == game.hostId

    LaunchedEffect(Unit) {
        viewModel.initialize(gameId)
    }

    deleteGame {
        viewModel.onExitClick(popUpScreen)
    }

    BasicToolbar(
        title = game.title.uppercase()
    )

    if (game.hostId.isEmpty() || game.guestId.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Waiting for an opponent.",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    } else if (game.gameOver) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Opponent quit the game.",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    } else {
        if (!game.cards.isEmpty() && !game.discardPile.isEmpty()) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(20.0.dp))

                LazyRow {
                    if (currentUserIsHost) {
                        items(game.guestHand.toList(), key = { it.id }) { cardItem ->
                            UnoCardBackEditor(
                                cardColor = Color.DarkGray,
                                modifier = Modifier.handCard()
                            )
                        }
                    } else {
                        items(game.hostHand.toList(), key = { it.id }) { cardItem ->
                            UnoCardBackEditor(
                                cardColor = Color.DarkGray,
                                modifier = Modifier.handCard()
                            )
                        }
                    }
                }

                Spacer(Modifier.height(20.0.dp))

                PlayableCard(game.discardPile[0])

                Spacer(Modifier.height(2.dp))

                BasicButton(AppText.drawCard, Modifier.basicButton()) {
                    viewModel.onDrawCardClick(currentUserIsHost)
                }

                Spacer(Modifier.height(12.0.dp))

                LazyRow {
                    if (currentUserIsHost) {
                        items(game.hostHand.toList(), key = { it.id }) { cardItem ->
                            CardInHand(
                                card = cardItem,
                                onClick = {
                                    if (cardItem.content != "+4") {
                                        viewModel.onCardClick(cardItem, currentUserIsHost, "")
                                    }
                                },
                                onActionClick = { actionColor ->
                                    if (cardItem.content == "+4") {
                                        //var plusFourColor = viewModel.onPlusFourClick(actionColor)
                                        viewModel.onCardClick(cardItem, currentUserIsHost, actionColor)
                                    }
                                }
                            )
                        }
                    } else {
                        items(game.guestHand.toList(), key = { it.id }) { cardItem ->
                            CardInHand(
                                card = cardItem,
                                onClick = {
                                    if (cardItem.content != "+4") {
                                        viewModel.onCardClick(cardItem, currentUserIsHost, "")
                                    }
                                },
                                onActionClick = { actionColor ->
                                    if (cardItem.content == "+4") {
                                        //var plusFourColor = viewModel.onPlusFourClick(actionColor)
                                        viewModel.onCardClick(cardItem, currentUserIsHost, actionColor)
                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(20.0.dp))

                if (currentUserIsHost && game.hostsMove) {
                    PlayersTurnText("Your turn!")
                } else if (!currentUserIsHost && !game.hostsMove) {
                    PlayersTurnText("Your turn!")
                } else {
                    PlayersTurnText("Opponent's turn!")
                }
            }
        }
    }

    DisposableEffect(viewModel) {
        viewModel.addListener()
        onDispose { viewModel.removeListener() }
    }
}

@ExperimentalMaterialApi
@Composable
private fun PlayableCard(card: Card) {
    var color: Color = Color.Black

    if (card.color == "red") {
        color = Red
    } else if (card.color == "green") {
        color = Green
    } else if (card.color == "yellow") {
        color = Yellow
    } else if (card.color == "blue") {
        color = Blue
    }

    UnoCardEditor(cardContent = card.content, cardColor = color, modifier = Modifier.playableCard(), onClick = {}) {
        //
    }
}

@Composable
private fun PlayersTurnText(text: String) {
    Text(
        text = text,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold
    )
}

@ExperimentalMaterialApi
@Composable
private fun deleteGame(deleteGame: () -> Unit) {
    var showWarningDialog by remember { mutableStateOf(false) }

    BackHandler {
        showWarningDialog = true
    }

    if(showWarningDialog) {
        AlertDialog(
            title = { Text(stringResource(AppText.leaveGame)) },
            text = { Text(stringResource(AppText.leaveGameDescription)) },
            dismissButton = { DialogCancelButton(AppText.cancel) { showWarningDialog = false } },
            confirmButton = {
                DialogConfirmButton(AppText.endGame) {
                    deleteGame()
                    showWarningDialog = false
                }
            },
            onDismissRequest = { showWarningDialog = false }
        )
    }
}


@Preview
@ExperimentalMaterialApi
@Composable
fun Preview_MyComposable() {
    PlayableCard(card = Card("", content = "1", color = "blue"))
}