package com.chavan.automessagereplier.notification_service

import android.service.notification.StatusBarNotification
import kotlinx.coroutines.delay
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

private const val THROTTLE_DELAY_MS = 3000L

class NotificationHandler {

    private val processing = AtomicBoolean(false)
    private val lastNotificationTime = AtomicLong(0)

    suspend fun processNotification(
        notification: StatusBarNotification,
        action: suspend () -> Unit
    ) {
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - lastNotificationTime.get()

        if (elapsedTime < THROTTLE_DELAY_MS && processing.getAndSet(true)) {
            delay(THROTTLE_DELAY_MS - elapsedTime)
        }

        action.invoke()

        lastNotificationTime.set(currentTime)
        processing.set(false)
    }
}
