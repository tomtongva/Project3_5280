package com.group3.project2.screens.game

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.group3.project2.R
import com.group3.project2.common.composable.*
import com.group3.project2.common.ext.playableCard
import com.group3.project2.common.ext.toolbarActions
import com.group3.project2.model.Card
import com.group3.project2.screens.lobby.GameItem
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

    LaunchedEffect(Unit) {
        viewModel.initialize(gameId)
    }

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
            endAction = { viewModel.onDoneClick(popUpScreen) }
        )

        Spacer(Modifier.height(40.0.dp))

        if (!game.cards.isEmpty()) {
            PlayableCard(game.cards[0])
        }

        Spacer(Modifier.height(40.0.dp))

//        LazyRow {
//            items(tasks.values.toList(), key = { it.id }) { taskItem ->
//                GameItem(
//                    task = taskItem,
//                    onClick = { viewModel.onGameClick(openScreen) },
//                )
//            }
//        }

    }
}

@ExperimentalMaterialApi
@Composable
private fun PlayableCard(card: Card) {
    var showWarningDialog by remember { mutableStateOf(false) }

    var color = Color.Red
    if (card.color == "red") {
        color = Color.Red
    } else if (card.color == "green") {
        color = Color.Green
    } else if (card.color == "yellow") {
        color = Color.Yellow
    } else if (card.color == "blue") {
        color = Color.Blue
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