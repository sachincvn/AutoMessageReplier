package com.chavan.automessagereplier.domain.usecase

import android.app.Notification
import android.service.notification.StatusBarNotification
import com.chavan.automessagereplier.core.utils.StringChecker.isName
import com.chavan.automessagereplier.data.local.ReceivedPattern
import com.chavan.automessagereplier.data.local.ReplyToOption
import com.chavan.automessagereplier.data.remote.dto.openai.ChatRequestDTO
import com.chavan.automessagereplier.data.remote.dto.openai.MessageX
import com.chavan.automessagereplier.data.remote.services.openapi.IOpenaiGptApi
import com.chavan.automessagereplier.domain.model.CustomMessage
import com.chavan.automessagereplier.domain.repository.CustomMessageRepo
import com.chavan.automessagereplier.notification_service.NotificationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AutoMessageReplierUseCase @Inject constructor(
    private val customMessageRepo: CustomMessageRepo,
    private val iOpenaiGptApi: IOpenaiGptApi
) {
    suspend fun isServiceEnabled(): Boolean {
        return customMessageRepo.getCustomMessageConfig().isActive
    }

    suspend fun getReplyMessage(statusBarNotification: StatusBarNotification): String? {
        val notificationTitle = statusBarNotification.notification.extras?.getCharSequence(Notification.EXTRA_TITLE)?.toString() ?: ""
        val notificationText = statusBarNotification.notification.extras?.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: ""

        val customMessages = customMessageRepo.getCustomMessages()
        val activeCustomMessages = customMessages.filter { it.isActive }

        return activeCustomMessages.firstNotNullOfOrNull { customMessage ->
            when (customMessage.replyToOption) {
                ReplyToOption.All -> replyMessage(notificationText, customMessage)
                ReplyToOption.SavedContact -> if (notificationTitle.isName()) replyMessage(notificationText, customMessage) else null
                ReplyToOption.UnknownContact -> if (!notificationTitle.isName()) replyMessage(notificationText, customMessage) else null
                ReplyToOption.Group -> if (NotificationUtils.isGroupMessageAndReplyAllowed(statusBarNotification)) replyMessage(notificationText, customMessage) else null
                ReplyToOption.SpecificContacts -> {
                    if (customMessage.selectedContacts.contains(notificationTitle)) replyMessage(notificationText, customMessage) else null
                }
            }
        }
    }

    private suspend fun replyMessage(notificationText: String, customMessage: CustomMessage): String? {
        return if (customMessage.replyWithChatGptApi) {
            withContext(Dispatchers.IO) {
                val response = getOpenApiReply(notificationText)
                if (response?.isNotEmpty() == true) response else customMessage.replyMessage
            }
        } else {
            when (customMessage.receivedPattern) {
                ReceivedPattern.ExactMatch -> if (notificationText == customMessage.receivedMessage) customMessage.replyMessage else null
                ReceivedPattern.Contains -> if (notificationText.contains(customMessage.receivedMessage)) customMessage.replyMessage else null
                ReceivedPattern.StartsWith -> if (notificationText.startsWith(customMessage.receivedMessage)) customMessage.replyMessage else null
                ReceivedPattern.EndsWith -> if (notificationText.endsWith(customMessage.receivedMessage)) customMessage.replyMessage else null
                ReceivedPattern.SimilarMatch -> if (notificationText.contentEquals(customMessage.receivedMessage)) customMessage.replyMessage else null
                ReceivedPattern.AnyMessage -> customMessage.replyMessage
            }
        }
    }

    private suspend fun getOpenApiReply(message: String): String? {
        val chatRequestDTO = ChatRequestDTO(
            messages = listOf(MessageX(role = "user", content = message)),
            model = "gpt-3.5-turbo",
            temperature = 0.7
        )
        val response = iOpenaiGptApi.getOpenAiResponse(chatRequestDTO)
        return response.choices.firstOrNull()?.message?.content
    }
}
