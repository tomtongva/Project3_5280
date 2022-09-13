package com.group3.project2.common.ext

import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun Modifier.textButton(): Modifier {
    return this.fillMaxWidth().padding(16.dp, 8.dp, 16.dp, 0.dp)
}

fun Modifier.basicButton(): Modifier {
    return this.fillMaxWidth().padding(8.dp, 8.dp)
}

fun Modifier.card(): Modifier {
    return this.padding(16.dp, 0.dp, 16.dp, 8.dp)
}

fun Modifier.playableCard(): Modifier {
    return this.padding(16.dp, 0.dp, 16.dp, 8.dp).height(180.dp).width(120.dp)
}

fun Modifier.handCard(): Modifier {
    return this.padding(5.dp, 5.dp, 5.dp, 5.dp).height(100.dp).width(66.dp)
}

fun Modifier.contextMenu(): Modifier {
    return this.fillMaxSize()
}

fun Modifier.dropdownSelector(): Modifier {
    return this.fillMaxWidth()
}

fun Modifier.fieldModifier(): Modifier {
    return this.fillMaxWidth().padding(16.dp, 4.dp)
}

fun Modifier.toolbarActions(): Modifier {
    return this.wrapContentSize(Alignment.TopEnd)
}

fun Modifier.spacer(): Modifier {
    return this.fillMaxWidth().padding(12.dp)
}

fun Modifier.smallSpacer(): Modifier {
    return this.fillMaxWidth().height(8.dp)
}
