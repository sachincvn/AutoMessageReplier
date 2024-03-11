/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chavan.automessagereplier.presentation.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chavan.automessagereplier.domain.model.CustomMessage
import com.chavan.automessagereplier.ui.theme.MontserratFontFamily

@Composable
fun CheckboxRow(
    customMessage: CustomMessage,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onOptionSelected: () -> Unit = {},
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = if (selected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline
            }
        ),
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .clickable { }
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val receivedMessage = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontFamily = MontserratFontFamily)) {
                        append("Received : ")
                    }
                    withStyle(style = SpanStyle(fontFamily = MontserratFontFamily)){
                        append(customMessage.receivedMessage)
                    }
                }
                val replyMessage = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontFamily = MontserratFontFamily)) {
                        append("Reply : ")
                    }
                    withStyle(style = SpanStyle(fontFamily = MontserratFontFamily)){
                        append(customMessage.replyMessage)
                    }
                }
                Text(receivedMessage, maxLines = 1)
                Spacer(modifier = Modifier.height(6.dp))
                Text(replyMessage, maxLines = 1)
                Row() {
                    val repliesTo = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontFamily = MontserratFontFamily)) {
                            append("To : ")
                        }
                        withStyle(style = SpanStyle(fontFamily = MontserratFontFamily)){
                            append("Sachin, Akash, Pawan kumar")
                        }
                    }
                    Text(text = repliesTo, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            Box(Modifier.padding(8.dp)) {
                Checkbox(selected, onCheckedChange = { onOptionSelected() })
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CheckboxRowPreview(){
    CheckboxRow(CustomMessage(
        replyMessage = "Hello",
        receivedMessage = "Welcome"
    ), selected = false)
}