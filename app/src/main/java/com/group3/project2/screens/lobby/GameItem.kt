package com.group3.project2.screens.lobby

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.group3.project2.common.composable.DropdownContextMenu
import com.group3.project2.common.ext.contextMenu
import com.group3.project2.common.ext.hasDueDate
import com.group3.project2.common.ext.hasDueTime
import com.group3.project2.R.drawable as AppIcon
import com.group3.project2.model.Task
import com.group3.project2.theme.Red
import java.lang.StringBuilder

@Composable
@ExperimentalMaterialApi
fun GameItem(
    task: Task,
    onClick: () -> Unit,
    onCheckChange: () -> Unit,
    onActionClick: (String) -> Unit
) {
    Card(
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Checkbox(
                checked = task.completed,
                onCheckedChange = { onCheckChange() },
                modifier = Modifier.padding(8.dp, 0.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(text = task.title, style = MaterialTheme.typography.subtitle2)
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(text = getDueDateAndTime(task), fontSize = 12.sp)
                }
            }

            if (task.flag) {
                Icon(
                    painter = painterResource(AppIcon.ic_flag),
                    tint = Red,
                    contentDescription = "Flag"
                )
            }

            DropdownContextMenu(GameActionOption.getOptions(), Modifier.contextMenu(), onActionClick)
        }
    }
}

private fun getDueDateAndTime(task: Task): String {
    val stringBuilder = StringBuilder("")

    if (task.hasDueDate()) {
        stringBuilder.append(task.dueDate)
        stringBuilder.append(" ")
    }

    if (task.hasDueTime()) {
        stringBuilder.append("at ")
        stringBuilder.append(task.dueTime)
    }

    return stringBuilder.toString()
}
