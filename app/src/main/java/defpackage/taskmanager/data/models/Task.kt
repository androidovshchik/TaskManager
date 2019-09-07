/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.models

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import defpackage.taskmanager.EXTRA_RESULT
import defpackage.taskmanager.EXTRA_TASK
import defpackage.taskmanager.R
import defpackage.taskmanager.extensions.pendingActivityFor
import defpackage.taskmanager.extensions.pendingReceiverFor
import defpackage.taskmanager.screens.task.TaskActivity
import defpackage.taskmanager.receivers.ActionReceiver
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import java.io.Serializable

@Entity(tableName = "Tasks")
class Task : Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    var id = 0L

    @ColumnInfo(name = "Название")
    lateinit var title: String

    @ColumnInfo(name = "Т-Время")
    var tTime: LocalTime? = null

    @ColumnInfo(name = "Т-День")
    var tDay: DayOfWeek? = null

    @ColumnInfo(name = "Т-Дата")
    var tDate: LocalDate? = null

    @ColumnInfo(name = "Т-Задача")
    var tTask: Long? = null

    @ColumnInfo(name = "Т-Повторы")
    var tRepeat: Int? = null

    @ColumnInfo(name = "Т-Задержка")
    var tDelay: Long? = null

    @ColumnInfo(name = "Сигнал")
    lateinit var signal: Signal

    @ColumnInfo(name = "Интервал повторения")
    var iRepeat = DEFAULT_REPEAT

    @ColumnInfo(name = "Интервал откладывания")
    var iDelay = DEFAULT_DELAY

    @ColumnInfo(name = "Статус")
    var status = false

    fun buildNotification(context: Context): Notification = context.run {
        return NotificationCompat.Builder(applicationContext, signal.name)
            .setSmallIcon(R.drawable.ic_notifications_white_24dp)
            .setContentTitle(title)
            .setContentIntent(
                pendingActivityFor<TaskActivity>(
                    EXTRA_TASK to id
                )
            )
            .setOngoing(true)
            .addAction(
                R.drawable.ic_done_white_24dp, "Выполнить", pendingReceiverFor<ActionReceiver>(
                    EXTRA_TASK to id,
                    EXTRA_RESULT to Record.STATUS_COMPLETED
                )
            )
            .addAction(
                R.drawable.ic_update_white_24dp, "Отложить", pendingReceiverFor<ActionReceiver>(
                    EXTRA_TASK to id,
                    EXTRA_RESULT to Record.STATUS_DEFERRED
                )
            )
            .addAction(
                R.drawable.ic_close_white_24dp, "Отменить", pendingReceiverFor<ActionReceiver>(
                    EXTRA_TASK to id,
                    EXTRA_RESULT to Record.STATUS_CANCELLED
                )
            )
            .also {
                if (signal == Signal.SOUNDLESS) {
                    it.setSound(null)
                }
            }
            .build()
    }

    companion object {

        private const val DEFAULT_REPEAT = 10L

        private const val DEFAULT_DELAY = 300L
    }
}