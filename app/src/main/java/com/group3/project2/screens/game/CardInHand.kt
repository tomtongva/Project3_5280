package com.group3.project2.screens.game

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.group3.project2.common.composable.UnoCardEditor
import com.group3.project2.common.ext.handCard
import com.group3.project2.model.Card
import com.group3.project2.theme.*

@Composable
@ExperimentalMaterialApi
fun CardInHand(
    card: Card,
    onClick: () -> Unit,
) {
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

    UnoCardEditor(
        cardContent = card.content,
        cardColor = color,
        modifier = Modifier.handCard(),
        onEditClick = onClick
    )
}