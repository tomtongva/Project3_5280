package com.group3.project2.screens.lobby

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.group3.project2.common.ext.card
import com.group3.project2.model.Game

@Composable
@ExperimentalMaterialApi
fun GameItem(
    game: Game,
    onClick: () -> Unit,
) {
    Card(
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier.card(),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "JOIN " + game.title.uppercase(),
                modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 16.dp).fillMaxWidth(),
                textAlign = TextAlign.Left)
        }
    }
}