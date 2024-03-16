package com.chavan.automessagereplier.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chavan.automessagereplier.data.local.custom_message.ReplyToOption
import com.chavan.automessagereplier.domain.model.CustomMessage

@Composable
fun CustomMessageItem(
    customMessage: CustomMessage,
    navigateToDetail: (Long) -> Unit,
    toggleActive: () -> Unit,
    toggleDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .semantics { selected = customMessage.isActive }
            .clip(CardDefaults.shape)
            .clickable {
                navigateToDetail(customMessage.id)
            },
        colors = CardDefaults.cardColors(
            containerColor = if (customMessage.isActive) MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularAvatar(
                    text =
                    if ((customMessage.replyToOption == ReplyToOption.SpecificContacts)
                        && (customMessage.selectedContacts[0].isNotBlank())
                    )
                        customMessage.selectedContacts[0]
                    else customMessage.replyToOption.value!!
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if ((customMessage.replyToOption == ReplyToOption.SpecificContacts)
                            && (customMessage.selectedContacts[0].isNotBlank())
                        )
                            "To : ${customMessage.selectedContacts[0]}"
                        else customMessage.replyToOption.value!!,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = if (customMessage.isActive) "active" else "in-active",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (customMessage.isActive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer
                    )
                }
                IconButton(onClick = toggleDelete) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                IconButton(
                    onClick = toggleActive,
                ) {
                    Icon(
                        imageVector = if (customMessage.isActive) Icons.Outlined.CheckCircle else Icons.Outlined.RemoveCircleOutline,
                        contentDescription = "Check",
                        tint = if (customMessage.isActive) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
                    )
                }
            }

            Text(
                "Received message: ${customMessage.receivedMessage}",
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
                color = if (customMessage.isActive) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
            )
            Text(
                if (customMessage.replyWithChatGptApi)
                    "Reply message:  Message will be auto generated by openai"
                else "Reply message:  ${customMessage.replyMessage}",
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
                color = if (customMessage.isActive) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurface,
            )

        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CustomMessageItemPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp)
    ) {
        CustomMessageItem(
            customMessage = CustomMessage(
                receivedMessage = "Hello",
                replyMessage = "Welcome",
                isActive = true
            ),
            navigateToDetail = {},
            toggleActive = { /*TODO*/ },
            toggleDelete = {}
        )
    }
}
