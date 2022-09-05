package com.group3.project2.screens.lobby

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.group3.project2.common.composable.ActionToolbar
import com.group3.project2.common.ext.smallSpacer
import com.group3.project2.common.ext.toolbarActions
import com.group3.project2.R.drawable as AppIcon
import com.group3.project2.R.string as AppText

@Composable
@ExperimentalMaterialApi
fun LobbyScreen(
    openScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LobbyViewModel = hiltViewModel()
) {
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(stringResource(AppText.new_game)) },
                onClick = { viewModel.onAddClick(openScreen) },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                modifier = modifier.padding(16.dp)
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        val tasks = viewModel.tasks

        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
            ActionToolbar(
                title = AppText.lobby,
                modifier = Modifier.toolbarActions(),
                endActionIcon = AppIcon.ic_settings,
                endAction = { viewModel.onSettingsClick(openScreen) }
            )

            Spacer(modifier = Modifier.smallSpacer())

            LazyColumn {
                items(tasks.values.toList(), key = { it.id }) { taskItem ->
                    GameItem(
                        task = taskItem,
                        onCheckChange = { viewModel.onTaskCheckChange(taskItem) },
                        onActionClick = { action ->
                            viewModel.onTaskActionClick(openScreen, taskItem, action)
                        }
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