package com.chavan.automessagereplier.notification_service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.service.notification.StatusBarNotification
import android.text.SpannableString
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.UUID

object NotificationUtils {
    private const val MAX_OLD_NOTIFICATION_CAN_BE_REPLIED_TIME_MS = 2 * 60 * 1000

    fun extractWearNotification(statusBarNotification: StatusBarNotification): NotificationWear {
        val wearableExtender = NotificationCompat.WearableExtender(statusBarNotification.notification)
        val actions = wearableExtender.actions
        val remoteInputs: MutableList<androidx.core.app.RemoteInput> = ArrayList(actions.size)
        var pendingIntent: PendingIntent? = null
        for (act in actions) {
            if (act != null && act.remoteInputs != null) {
                for (x in act.remoteInputs!!.indices) {
                    val remoteInput = act.remoteInputs!![x]
                    remoteInputs.add(remoteInput)
                    pendingIntent = act.actionIntent
                }
            }
        }
        return NotificationWear(
            statusBarNotification.packageName,
            pendingIntent,
            remoteInputs,
            statusBarNotification.notification.extras,
            statusBarNotification.tag,
            UUID.randomUUID().toString()
        )
    }

    fun getTitleRaw(sbn: StatusBarNotification): String? {
        return sbn.notification.extras.getString("android.title")
    }

    fun isNewNotification(sbn: StatusBarNotification): Boolean {
        return sbn.notification.`when` == 0L ||
                System.currentTimeMillis() - sbn.notification.`when` < MAX_OLD_NOTIFICATION_CAN_BE_REPLIED_TIME_MS
    }

    private fun isNotificationAccessGranted(context: Context,packageName:String): Boolean {
        val notificationListenerSet = NotificationManagerCompat.getEnabledListenerPackages(context)
        return notificationListenerSet.contains(packageName)
    }

    fun requestNotificationPermission(context: Context,packageName:String) {
        if (!isNotificationAccessGranted(context,packageName)) {
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            context.startActivity(intent)
        }
    }

    fun isGroupMessageAndReplyAllowed(sbn: StatusBarNotification): Boolean {
        val groupMessageReplyEnabled = false
        val rawTitle = getTitleRaw(sbn)
        val rawText = SpannableString.valueOf("" + sbn.notification.extras["android.text"])
        val isPossiblyAnImageGrpMsg = (rawTitle != null && ": ".contains(rawTitle)
                && rawText != null && rawText.toString().startsWith("\uD83D\uDCF7"))
        return if (!sbn.notification.extras.getBoolean("android.isGroupConversation")) {
            !isPossiblyAnImageGrpMsg
        } else {
            return groupMessageReplyEnabled
        }
    }

}