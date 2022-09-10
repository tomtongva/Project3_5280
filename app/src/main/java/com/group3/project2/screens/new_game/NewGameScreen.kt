package com.group3.project2.screens.new_game

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.group3.project2.common.composable.*
import com.group3.project2.common.ext.fieldModifier
import com.group3.project2.common.ext.spacer
import com.group3.project2.common.ext.toolbarActions
import com.group3.project2.R.drawable as AppIcon
import com.group3.project2.R.string as AppText

@Composable
@ExperimentalMaterialApi
fun NewGameScreen(
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NewGameViewModel = hiltViewModel()
) {
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(stringResource(AppText.new_game_create)) },
                onClick = { viewModel.onDoneClick(openAndPopUp) },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                modifier = modifier.padding(16.dp)
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        val game by viewModel.game

        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BasicToolbar(AppText.new_game)

            Spacer(modifier = Modifier.spacer())

            val fieldModifier = Modifier.fieldModifier()
            BasicField(AppText.title, game.title, viewModel::onTitleChange, fieldModifier)
        }
    }
}
