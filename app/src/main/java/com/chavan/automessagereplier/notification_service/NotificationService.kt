package com.chavan.automessagereplier.notification_service

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.chavan.automessagereplier.domain.usecase.AutoMessageReplierUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationService : NotificationListenerService() {

    private var lastNotificationTitle: CharSequence? = null
    private var lastNotificationTime: Long = 0
    private val notificationHandler = NotificationHandler()

    @Inject
    lateinit var autoMessageReplierUseCase: AutoMessageReplierUseCase

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        CoroutineScope(Dispatchers.IO).launch {
            notificationHandler.processNotification(sbn) {

                if (!autoMessageReplierUseCase.isServiceEnabled()) {
                    return@processNotification
                }
                val currentTime = System.currentTimeMillis()
                val currentTitle = sbn.notification.extras.getCharSequence(Notification.EXTRA_TITLE)

                if (currentTitle != null && currentTitle == lastNotificationTitle &&
                    (currentTime - lastNotificationTime) < 3000
                ) {
                    return@processNotification
                } else {
                    handleNotification(sbn)
                }
            }
        }
    }

    private fun handleNotification(sbn: StatusBarNotification) {
        CoroutineScope(Dispatchers.IO).launch {
            if ("com.whatsapp" == sbn.packageName) {
                val message = autoMessageReplierUseCase.getReplyMessage(sbn)

                if (!message.isNullOrEmpty()) {
                    sendReply(sbn = sbn, message = message)
                } else {
                    return@launch
                }
            }
            updateLastNotification(sbn)
        }
    }


    private fun sendReply(
        sbn: StatusBarNotification,
        message: String
    ) {
        val (_, pendingIntent, remoteInput) = NotificationUtils.extractWearNotification(sbn)
        if (remoteInput.isEmpty()) {
            return
        }

        val remoteInputs = arrayOfNulls<androidx.core.app.RemoteInput>(remoteInput.size)
        val localIntent = Intent()
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val localBundle = Bundle()


        for ((i, remoteIn) in remoteInput.withIndex()) {
            remoteInputs[i] = remoteIn
            localBundle.putCharSequence(
                remoteInputs[i]!!.resultKey,
                message
            )
        }

        androidx.core.app.RemoteInput.addResultsToIntent(remoteInputs, localIntent, localBundle)
        try {
            if (pendingIntent != null) {
                pendingIntent.send(this, 0, localIntent)
                cancelNotification(sbn.key)
            }
        } catch (e: PendingIntent.CanceledException) {
            Log.e("Error", "Error while replying to last notification : " + e.localizedMessage)
        }
    }


    private fun updateLastNotification(sbn: StatusBarNotification) {
        lastNotificationTitle = sbn.notification.extras.getCharSequence(Notification.EXTRA_TITLE)
        lastNotificationTime = System.currentTimeMillis()
    }

}