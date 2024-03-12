package com.chavan.automessagereplier.domain.usecase

import com.chavan.automessagereplier.data.local.ReceivedPattern
import com.chavan.automessagereplier.data.local.ReplyToOption
import com.chavan.automessagereplier.domain.repository.CustomMessageRepo
import javax.inject.Inject

class AutoMessageReplierUseCase  @Inject constructor(
    private val customMessageRepo: CustomMessageRepo
) {

    suspend fun isServiceEnabled() : Boolean{
        return customMessageRepo.getCustomMessageConfig().isActive
    }

    suspend fun replyMessage(notificationTitle : String, notificationText : String) : String? {
        var replyMessage: String?  = null
        val customMessages = customMessageRepo.getCustomMessages()
        val activeCustomMessages = customMessages.filter { it.isActive }
        activeCustomMessages.forEach {
            when(it.receivedPattern){
                ReceivedPattern.ExactMatch -> {
                    if (notificationText == it.receivedMessage){
                        replyMessage = it.replyMessage
                    }
                }
                ReceivedPattern.Contains -> {
                    if (notificationText.contains(it.receivedMessage)){
                        replyMessage = it.replyMessage
                    }
                }
                ReceivedPattern.StartsWith -> {
                    if (notificationText.startsWith(it.receivedMessage)){
                        replyMessage = it.replyMessage
                    }
                }
                ReceivedPattern.EndsWith -> {
                    if (notificationText.endsWith(it.receivedMessage)){
                        replyMessage = it.replyMessage
                    }
                }
                ReceivedPattern.SimilarMatch -> {
                    if (notificationText.contentEquals(it.receivedMessage)){
                        replyMessage = it.replyMessage
                    }
                }
                ReceivedPattern.AnyMessage -> replyMessage = it.replyMessage
            }
        }
        return replyMessage
    }
}