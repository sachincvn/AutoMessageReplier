package com.chavan.automessagereplier.domain.usecase

import android.app.Notification
import android.service.notification.StatusBarNotification
import com.chavan.automessagereplier.core.utils.StringChecker.isName
import com.chavan.automessagereplier.data.local.ReceivedPattern
import com.chavan.automessagereplier.data.local.ReplyToOption
import com.chavan.automessagereplier.data.remote.dto.openai.ChatRequestDTO
import com.chavan.automessagereplier.data.remote.dto.openai.MessageX
import com.chavan.automessagereplier.data.remote.dto.openai.OpenAiGptDTO
import com.chavan.automessagereplier.data.remote.services.openapi.IOpenaiGptApi
import com.chavan.automessagereplier.domain.repository.CustomMessageRepo
import com.chavan.automessagereplier.notification_service.NotificationUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AutoMessageReplierUseCase @Inject constructor(
    private val customMessageRepo: CustomMessageRepo,
    private val iOpenaiGptApi: IOpenaiGptApi
) {
    private suspend fun getOpenApiReply(message: String) : String? {
        val chatRequestDTO = ChatRequestDTO(
            messages = listOf(MessageX(role = "user", content = message)),
            model = "gpt-3.5-turbo",
            temperature = 0.7
        )
        var replyMessage : String? = null
        val  response = iOpenaiGptApi.getOpenAiResponse(chatRequestDTO)
        if (response.choices[0].message.content.isNotEmpty()) {
            replyMessage = response.choices[0].message.content
        }
        return replyMessage
    }

    suspend fun isServiceEnabled(): Boolean {
        return customMessageRepo.getCustomMessageConfig().isActive
    }

    suspend fun getReplyMessage(statusBarNotification: StatusBarNotification): String? {

        val notificationTitle = statusBarNotification.notification.extras.getCharSequence(Notification.EXTRA_TITLE).toString()
        val notificationText = statusBarNotification.notification.extras.getCharSequence(Notification.EXTRA_TEXT).toString()

        var replyMessage: String? = null
        val customMessages = customMessageRepo.getCustomMessages()
        val activeCustomMessages = customMessages.filter { it.isActive }
        activeCustomMessages.forEach { customMessage ->
            when(customMessage.replyToOption){
                ReplyToOption.All -> {
                    replyMessage = replyMessage(notificationText,customMessage.receivedPattern,customMessage.receivedMessage,customMessage.replyMessage,customMessage.replyWithChatGptApi)
                }
                ReplyToOption.SavedContact -> {
                    if (notificationTitle.isName()){
                        replyMessage = replyMessage(notificationText,customMessage.receivedPattern,customMessage.receivedMessage,customMessage.replyMessage,customMessage.replyWithChatGptApi)
                    }
                }
                ReplyToOption.UnknownContact -> {
                    if (!notificationTitle.isName()){
                        replyMessage = replyMessage(notificationText,customMessage.receivedPattern,customMessage.receivedMessage,customMessage.replyMessage,customMessage.replyWithChatGptApi)
                    }
                }
                ReplyToOption.Group -> {
                    if (NotificationUtils.isGroupMessageAndReplyAllowed(statusBarNotification)){
                        replyMessage = replyMessage(notificationText,customMessage.receivedPattern,customMessage.receivedMessage,customMessage.replyMessage,customMessage.replyWithChatGptApi)
                    }
                }
                ReplyToOption.SpecificContacts -> {
                    if (customMessage.selectedContacts.isNotEmpty()){
                        val isAddedToSpecificContact = customMessage.selectedContacts.any {
                            it == notificationTitle
                        }
                        if (isAddedToSpecificContact){
                            replyMessage = replyMessage(notificationText,customMessage.receivedPattern,customMessage.receivedMessage,customMessage.replyMessage,customMessage.replyWithChatGptApi)
                        }
                    }
                }
            }
        }
        return replyMessage
    }

    private fun replyMessage(
        notificationText: String,
        receivedPattern : ReceivedPattern,
        receivedMessage : String,
        replyMessage : String,
        replyWithChatGptApi : Boolean = false,
    ) : String? {
        var message : String? = null
        if (replyWithChatGptApi){
            CoroutineScope(Dispatchers.Main).launch {
                when (receivedPattern) {
                    ReceivedPattern.ExactMatch -> {
                        if (notificationText == receivedMessage) {
                            message = getOpenApiReply(receivedMessage)
                        }
                    }

                    ReceivedPattern.Contains -> {
                        if (notificationText.contains(receivedMessage)) {
                            message = getOpenApiReply(receivedMessage)
                        }
                    }

                    ReceivedPattern.StartsWith -> {
                        if (notificationText.startsWith(receivedMessage)) {
                            message = getOpenApiReply(receivedMessage)
                        }
                    }

                    ReceivedPattern.EndsWith -> {
                        if (notificationText.endsWith(receivedMessage)) {
                            message = getOpenApiReply(receivedMessage)
                        }
                    }

                    ReceivedPattern.SimilarMatch -> {
                        if (notificationText.contentEquals(receivedMessage)) {
                            message = getOpenApiReply(receivedMessage)
                        }
                    }

                    ReceivedPattern.AnyMessage -> message = getOpenApiReply(receivedMessage)
                }
            }
        }
        else{
            when (receivedPattern) {
                ReceivedPattern.ExactMatch -> {
                    if (notificationText == receivedMessage) {
                        message = replyMessage
                    }
                }

                ReceivedPattern.Contains -> {
                    if (notificationText.contains(receivedMessage)) {
                        message = replyMessage
                    }
                }

                ReceivedPattern.StartsWith -> {
                    if (notificationText.startsWith(receivedMessage)) {
                        message = replyMessage
                    }
                }

                ReceivedPattern.EndsWith -> {
                    if (notificationText.endsWith(receivedMessage)) {
                        message = replyMessage
                    }
                }

                ReceivedPattern.SimilarMatch -> {
                    if (notificationText.contentEquals(receivedMessage)) {
                        message = replyMessage
                    }
                }

                ReceivedPattern.AnyMessage -> message = replyMessage
            }
        }
        return message
    }
}