/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.models

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import defpackage.taskmanager.R
import defpackage.taskmanager.extensions.pendingActivityFor
import defpackage.taskmanager.extensions.pendingReceiverFor
import defpackage.taskmanager.screens.task.TaskActivity
import defpackage.taskmanager.services.ActionReceiver

enum class Signal(val id: Long, private val description: String) {
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

    fun buildNotification(context: Context, task: Task, record: Record): Notification {
        return NotificationCompat.Builder(context, name)
            .setSmallIcon(R.drawable.ic_notifications_white_24dp)
            .setContentTitle(task.title)
            .setContentIntent(
                context.pendingActivityFor<TaskActivity>(
                    TaskActivity.EXTRA_TASK to task.id
                )
            )
            .setOngoing(true)
            .addAction(
                R.drawable.ic_done_white_24dp, "Выполнить", context.pendingReceiverFor<ActionReceiver>(
                    ActionReceiver.EXTRA_TASK to task.id,
                    ActionReceiver.EXTRA_RESULT to true
                )
            )
            .addAction(
                R.drawable.ic_schedule_white_24dp, "Отложить", context.pendingReceiverFor<ActionReceiver>(
                    ActionReceiver.EXTRA_TASK to task.id,
                    ActionReceiver.EXTRA_RESULT to null
                )
            )
            .addAction(
                R.drawable.ic_close_white_24dp, "Отменить", context.pendingReceiverFor<ActionReceiver>(
                    ActionReceiver.EXTRA_TASK to task.id,
                    ActionReceiver.EXTRA_RESULT to false
                )
            )
            .also {
                if (this == SOUNDLESS) {
                    it.setSound(null)
                }
            }
            .build()
    }

    companion object {

        @JvmStatic
        val map = values().associateBy(Signal::id)

        @JvmStatic
        fun fromId(value: Long?): Signal? = value?.let { map[value] }
    }
}