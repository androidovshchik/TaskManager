/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.models

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.os.Build
import androidx.core.app.NotificationManagerCompat

enum class Behavior(val id: Long, private val description: String) {
    SOUNDLESS(1, "Без звука"),
    VIBRATION(2, "С вибрацией"),
    SOUND(3, "Со звуком"),
    SOUND_VIBRATION(4, "С вибрацией и звуком"),
    DEFAULT(5, "По умолчанию");

    @TargetApi(Build.VERSION_CODES.O)
    fun buildChannel(): NotificationChannel {
        return NotificationChannel(
            name, description, if (this == SOUNDLESS) {
                NotificationManagerCompat.IMPORTANCE_LOW
            } else {
                NotificationManagerCompat.IMPORTANCE_DEFAULT
            }
        ).also {
            it.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
    }

    companion object {

        @JvmStatic
        val map = values().associateBy(Behavior::id)

        @JvmStatic
        fun fromId(value: Long?): Behavior? = value?.let { map[value] }
    }
}