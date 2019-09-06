/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

@file:Suppress("unused")

package defpackage.taskmanager.extensions

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color

const val SILENT_CHANNEL_ID = "app_silent_channel"
const val NOISY_CHANNEL_ID = "app_noisy_channel"

fun NotificationManager.createSilentChannel() {
    if (isOreoPlus()) {
        createNotificationChannel(NotificationChannel(
            SILENT_CHANNEL_ID, SILENT_CHANNEL_ID,
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            lockscreenVisibility = Notification.VISIBILITY_SECRET
        })
    }
}

fun NotificationManager.createNoisyChannel(color: Int = Color.BLUE) {
    if (isOreoPlus()) {
        createNotificationChannel(NotificationChannel(
            NOISY_CHANNEL_ID, NOISY_CHANNEL_ID,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            enableLights(true)
            enableVibration(true)
            lightColor = color
            vibrationPattern = longArrayOf(1000, 1000)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        })
    }
}