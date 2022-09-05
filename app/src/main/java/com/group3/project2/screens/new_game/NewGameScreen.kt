package com.group3.project2.screens.new_game

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.group3.project2.common.composable.*
import com.group3.project2.common.ext.card
import com.group3.project2.common.ext.fieldModifier
import com.group3.project2.common.ext.spacer
import com.group3.project2.common.ext.toolbarActions
import com.group3.project2.model.Priority
import com.group3.project2.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.group3.project2.R.drawable as AppIcon
import com.group3.project2.R.string as AppText

@Composable
@ExperimentalMaterialApi
fun NewGameScreen(
    popUpScreen: () -> Unit,
    taskId: String,
    modifier: Modifier = Modifier,
    viewModel: NewGameViewModel = hiltViewModel()
) {
    val task by viewModel.task
    val game by viewModel.task

    LaunchedEffect(Unit) {
        viewModel.initialize(taskId)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ActionToolbar(
            title = AppText.new_game,
            modifier = Modifier.toolbarActions(),
            endActionIcon = AppIcon.ic_baseline_add_24,
            endAction = { viewModel.onDoneClick(popUpScreen) }
        )

        Spacer(modifier = Modifier.spacer())

        val fieldModifier = Modifier.fieldModifier()
        BasicField(AppText.title, task.title, viewModel::onTitleChange, fieldModifier)
    }
}
