package com.group3.project2.screens.game

import androidx.compose.foundation.layout.*
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
import com.group3.project2.common.composable.BasicToolbar
import com.group3.project2.common.composable.DialogCancelButton
import com.group3.project2.common.composable.DialogConfirmButton
import com.group3.project2.common.composable.UnoCardEditor
import com.group3.project2.common.ext.playableCard
import com.group3.project2.R.string as AppText

@ExperimentalMaterialApi
@Composable
fun GameScreen(
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    BasicToolbar(AppText.app_name)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlayableCard()

        Spacer(Modifier.height(40.0.dp))

        //PlayableCard { viewModel.onSignInClick(openAndPopUp) }
    }
}

@ExperimentalMaterialApi
@Composable
private fun PlayableCard() {
    var showWarningDialog by remember { mutableStateOf(false) }

    UnoCardEditor(cardContent = "S", cardColor = Color.Red, modifier = Modifier.playableCard()) {

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
    PlayableCard()
}