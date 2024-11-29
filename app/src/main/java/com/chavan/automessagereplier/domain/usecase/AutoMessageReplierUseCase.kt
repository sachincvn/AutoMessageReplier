package com.chavan.automessagereplier.domain.usecase

import android.app.Notification
import android.service.notification.StatusBarNotification
import androidx.compose.ui.text.toLowerCase
import com.chavan.automessagereplier.core.utils.StringChecker.isName
import com.chavan.automessagereplier.data.local.custom_message.ReceivedPattern
import com.chavan.automessagereplier.data.local.custom_message.ReplyToOption
import com.chavan.automessagereplier.data.remote.dto.openai.ChatRequestDTO
import com.chavan.automessagereplier.data.remote.dto.openai.MessageX
import com.chavan.automessagereplier.domain.model.custom_message.CustomMessage
import com.chavan.automessagereplier.domain.repository.CustomMessageRepo
import com.chavan.automessagereplier.domain.repository.openapi.OpenAiApiRepo
import com.chavan.automessagereplier.notification_service.NotificationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AutoMessageReplierUseCase @Inject constructor(
    private val customMessageRepo: CustomMessageRepo,
    private val openAiApiRepo: OpenAiApiRepo
) {
    suspend fun isServiceEnabled(): Boolean {
        return try {
            customMessageRepo.getCustomMessageConfig()?.isActive ?: false
        }catch (ex : Exception){
            false
        }
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
                if (notificationText.isNotBlank() && notificationText.isNotBlank()){
                    val response = getOpenApiReply(notificationText)
                    if (response?.isNotEmpty() == true) response else customMessage.replyMessage
                }
                else{
                    null
                }
            }
        } else {
            when (customMessage.receivedPattern) {
                ReceivedPattern.ExactMatch -> if (notificationText == customMessage.receivedMessage) customMessage.replyMessage else null
                ReceivedPattern.Contains -> if (notificationText.contains(customMessage.receivedMessage)) customMessage.replyMessage else null
                ReceivedPattern.StartsWith -> if (notificationText.lowercase().startsWith(customMessage.receivedMessage.lowercase())) customMessage.replyMessage else null
                ReceivedPattern.EndsWith -> if (notificationText.lowercase().endsWith(customMessage.receivedMessage.lowercase())) customMessage.replyMessage else null
                ReceivedPattern.SimilarMatch -> if (notificationText.lowercase().contentEquals(customMessage.receivedMessage.lowercase())) customMessage.replyMessage else null
                ReceivedPattern.AnyMessage -> customMessage.replyMessage
            }
        }
    }

    private suspend fun getOpenApiReply(message: String): String? {
        var errorMessage : String? = null
        try {
            val result = openAiApiRepo.getOpenAiLocalConfig()
            if (result!=null){
                errorMessage = result.errorMessage
                val chatRequestDTO = ChatRequestDTO(
                    messages = listOf(MessageX(role = "user", content = message)),
                    model = result.openAiModel?.value ?: "gpt-3.5-turbo",
                    temperature = result.temperature ?: 7.0 ,
                )
                val response = withContext(Dispatchers.IO) {
                    openAiApiRepo.getOpenAiResponse("Bearer ${result.openAiApiKey}",chatRequestDTO)
                }
                return response.choices.firstOrNull()?.message?.content
            }
            return null
        }
        catch (ex : Exception){
            ex.printStackTrace()
            return errorMessage
        }
    }
}
