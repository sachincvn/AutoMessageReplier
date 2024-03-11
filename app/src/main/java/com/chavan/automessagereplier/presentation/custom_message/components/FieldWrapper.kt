package com.chavan.automessagereplier.presentation.custom_message.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp


@Composable
fun FieldWrapper(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    MaterialTheme.colorScheme.inverseOnSurface,
                ),
                shape = MaterialTheme.shapes.small
            )
            .clip(MaterialTheme.shapes.small)
            .background(
                color = MaterialTheme.colorScheme.inverseOnSurface
            )
            .padding(10.dp),
        ){
        content()
    }

}