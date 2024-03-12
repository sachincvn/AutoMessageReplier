package com.chavan.automessagereplier.domain.usecase

import android.app.Notification
import android.service.notification.StatusBarNotification
import com.chavan.automessagereplier.core.utils.StringChecker.isName
import com.chavan.automessagereplier.data.local.ReceivedPattern
import com.chavan.automessagereplier.data.local.ReplyToOption
import com.chavan.automessagereplier.domain.repository.CustomMessageRepo
import com.chavan.automessagereplier.notification_service.NotificationUtils
import javax.inject.Inject

class AutoMessageReplierUseCase @Inject constructor(
    private val customMessageRepo: CustomMessageRepo
) {

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
                    replyMessage = replyMessage(notificationText,customMessage.receivedPattern,customMessage.receivedMessage,customMessage.replyMessage)
                }
                ReplyToOption.SavedContact -> {
                    if (notificationTitle.isName()){
                        replyMessage = replyMessage(notificationText,customMessage.receivedPattern,customMessage.receivedMessage,customMessage.replyMessage)
                    }
                }
                ReplyToOption.UnknownContact -> {
                    if (!notificationTitle.isName()){
                        replyMessage = replyMessage(notificationText,customMessage.receivedPattern,customMessage.receivedMessage,customMessage.replyMessage)
                    }
                }
                ReplyToOption.Group -> {
                    if (NotificationUtils.isGroupMessageAndReplyAllowed(statusBarNotification)){
                        replyMessage = replyMessage(notificationText,customMessage.receivedPattern,customMessage.receivedMessage,customMessage.replyMessage)
                    }
                }
                ReplyToOption.SpecificContacts -> {
                    if (customMessage.selectedContacts.isNotEmpty()){
                        val isAddedToSpecificContact = customMessage.selectedContacts.any {
                            it == notificationTitle
                        }
                        if (isAddedToSpecificContact){
                            replyMessage = replyMessage(notificationText,customMessage.receivedPattern,customMessage.receivedMessage,customMessage.replyMessage)
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
    ) : String? {
        var message : String? = null
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
        return message
    }
}