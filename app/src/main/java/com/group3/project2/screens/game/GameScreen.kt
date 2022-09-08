package com.group3.project2.screens.game

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.group3.project2.R
import com.group3.project2.common.composable.*
import com.group3.project2.common.ext.basicButton
import com.group3.project2.common.ext.handCard
import com.group3.project2.common.ext.playableCard
import com.group3.project2.common.ext.toolbarActions
import com.group3.project2.model.Card
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

    if (game.hostId.isEmpty() || game.guestId.isEmpty()) {
        Text("Waiting for opponent")
    } else if (game.gameOver) {
        //
    } else {
        if (!game.cards.isEmpty() && !game.discardPile.isEmpty()) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                ActionToolbar(
                    title = game.title.uppercase(),
                    modifier = Modifier.toolbarActions(),
                    endActionIcon = R.drawable.ic_exit,
                    endAction = { viewModel.onExitClick(popUpScreen) }
                )

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
                                onClick = { viewModel.onCardClick(cardItem, currentUserIsHost) },
                            )
                        }
                    } else {
                        items(game.guestHand.toList(), key = { it.id }) { cardItem ->
                            CardInHand(
                                card = cardItem,
                                onClick = { viewModel.onCardClick(cardItem, currentUserIsHost) }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(20.0.dp))

                if (currentUserIsHost && game.hostsMove) {
                    Text(
                        text = "Your turn!",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else if (!currentUserIsHost && !game.hostsMove) {
                    Text(
                        text = "Your turn!",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
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
    var showWarningDialog by remember { mutableStateOf(false) }

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

    UnoCardEditor(cardContent = card.content, cardColor = color, modifier = Modifier.playableCard()) {

    }

    if(showWarningDialog) {
        AlertDialog(
            title = { Text(stringResource(AppText.sign_out_title)) },
            text = { Text(stringResource(AppText.sign_out_description)) },
            dismissButton = { DialogCancelButton(AppText.cancel) { showWarningDialog = false } },
            confirmButton = {
                DialogConfirmButton(AppText.sign_out) {
                    //signOut()
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