package com.group3.project2.common.composable

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.group3.project2.common.ext.contextMenu
import com.group3.project2.common.ext.dropdownSelector
import com.group3.project2.screens.game.PlusFourOption
import com.group3.project2.theme.Black
import com.group3.project2.theme.Red
import com.group3.project2.theme.White

@ExperimentalMaterialApi
@Composable
fun DangerousCardEditor(
    @StringRes title: Int,
    @DrawableRes icon: Int,
    content: String,
    modifier: Modifier,
    onEditClick: () -> Unit
) {
    CardEditor(title, icon, content, onEditClick, MaterialTheme.colors.primary, modifier)
}

@ExperimentalMaterialApi
@Composable
fun RegularCardEditor(
    @StringRes title: Int,
    @DrawableRes icon: Int,
    content: String,
    modifier: Modifier,
    onEditClick: () -> Unit
) {
    CardEditor(title, icon, content, onEditClick, MaterialTheme.colors.onSurface, modifier)
}

@ExperimentalMaterialApi
@Composable
private fun CardEditor(
    @StringRes title: Int,
    @DrawableRes icon: Int,
    content: String,
    onEditClick: () -> Unit,
    highlightColor: Color,
    modifier: Modifier
) {
    Card(backgroundColor = MaterialTheme.colors.onPrimary, modifier = modifier, onClick = onEditClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(stringResource(title), color = highlightColor)
            }

            if (content.isNotBlank()) {
                Text(text = content, modifier = Modifier.padding(16.dp, 0.dp))
            }

            Icon(
                painter = painterResource(icon),
                contentDescription = "Icon",
                tint = highlightColor
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun UnoCardEditor(
    cardContent: String,
    cardColor: Color,
    modifier: Modifier,
    onClick: () -> Unit,
    onActionClick: (String) -> Unit
) {
    Card(backgroundColor = cardColor, modifier = modifier, onClick = onClick) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxHeight().fillMaxWidth()
        ) {

            Box(
                modifier = Modifier.size(height = 60.dp, width = 500.dp).border(BorderStroke(3.dp, White), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = cardContent,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        shadow = Shadow(
                                    color = Color.Black,
                                    offset = Offset(4f, 4f),
                                    blurRadius = 4f
                                    )
                    ),
                    color = White,
                    fontSize = 40.sp
                )

                if(cardContent == "+4") {
                    DropdownContextMenu(
                        PlusFourOption.getOptions(),
                        Modifier.contextMenu(),
                        onActionClick
                    )
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun UnoCardBackEditor(
    cardColor: Color,
    modifier: Modifier
) {
    Card(backgroundColor = cardColor, modifier = modifier) {
    }
}

@Composable
@ExperimentalMaterialApi
fun CardSelector(
    @StringRes label: Int,
    options: List<String>,
    selection: String,
    modifier: Modifier,
    onNewValue: (String) -> Unit
) {
    Card(backgroundColor = MaterialTheme.colors.onPrimary, modifier = modifier) {
        DropdownSelector(label, options, selection, Modifier.dropdownSelector(), onNewValue)
    }
}
