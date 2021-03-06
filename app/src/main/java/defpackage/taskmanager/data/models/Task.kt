/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.models

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import defpackage.taskmanager.*
import defpackage.taskmanager.extensions.isNougatPlus
import defpackage.taskmanager.extensions.pendingActivityFor
import defpackage.taskmanager.extensions.pendingReceiverFor
import defpackage.taskmanager.receivers.ActionReceiver
import defpackage.taskmanager.screens.task.TaskActivity
import org.joda.time.LocalDate
import org.joda.time.LocalTime

@Entity(
    tableName = "Tasks",
    foreignKeys = [
        ForeignKey(
            entity = Week::class,
            parentColumns = ["ID"],
            childColumns = ["Т-День"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Task::class,
            parentColumns = ["ID"],
            childColumns = ["Т-Задача"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Signal::class,
            parentColumns = ["ID"],
            childColumns = ["Сигнал"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.SET_DEFAULT
        )
    ]
)
open class Task {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    var id = 0L

    @ColumnInfo(name = "Название")
    lateinit var title: String

    @ColumnInfo(name = "Т-Время")
    var tTime: LocalTime? = null

    @ColumnInfo(name = "Т-День", index = true)
    var tDay: DayOfWeek? = null

    @ColumnInfo(name = "Т-Дата")
    var tDate: LocalDate? = null

    @ColumnInfo(name = "Т-Задача", index = true)
    var tTask: Long? = null

    @ColumnInfo(name = "Т-Повторы")
    var tRepeat: Int? = null

    @ColumnInfo(name = "Т-Задержка")
    var tDelay: Long? = null

    @ColumnInfo(name = "Сигнал", index = true)
    lateinit var behavior: Behavior

    @ColumnInfo(name = "Интервал повторения")
    var iRepeat = DEFAULT_REPEAT

    @ColumnInfo(name = "Интервал откладывания")
    var iDelay = DEFAULT_DELAY

    @ColumnInfo(name = "Статус")
    var status = false

    val isValid: Boolean
        get() = tTime != null || tDay != null || tDate != null || tTask != null

    @Suppress("DEPRECATION")
    fun buildNotification(context: Context): Notification = context.run {
        return NotificationCompat.Builder(applicationContext, behavior.name)
            .setSmallIcon(R.drawable.ic_notifications_white_24dp)
            .setContentTitle(title)
            .setContentIntent(
                pendingActivityFor<TaskActivity>(
                    EXTRA_ID to id
                )
            )
            .setOngoing(true)
            .setSound(null)
            .addAction(
                R.drawable.ic_done_white_24dp, "Выполнить", pendingReceiverFor<ActionReceiver>(
                    EXTRA_ID to id,
                    EXTRA_ACTION to ACTION_COMPLETED
                )
            )
            .addAction(
                R.drawable.ic_update_white_24dp, "Отложить", pendingReceiverFor<ActionReceiver>(
                    EXTRA_ID to id,
                    EXTRA_ACTION to ACTION_DEFERRED
                )
            )
            .addAction(
                R.drawable.ic_close_white_24dp, "Отменить", pendingReceiverFor<ActionReceiver>(
                    EXTRA_ID to id,
                    EXTRA_ACTION to ACTION_CANCELLED
                )
            )
            .also {
                if (isNougatPlus()) {
                    it.priority = NotificationManager.IMPORTANCE_HIGH
                } else {
                    it.priority = Notification.PRIORITY_MAX
                }
            }
            .build()
    }

    override fun toString(): String {
        return "Task(id=$id, title='$title', tTime=$tTime, tDay=$tDay, tDate=$tDate, tTask=$tTask, tRepeat=$tRepeat, tDelay=$tDelay, behavior=$behavior, iRepeat=$iRepeat, iDelay=$iDelay, status=$status)"
    }

    companion object {

        private const val DEFAULT_REPEAT = 10L

        private const val DEFAULT_DELAY = 300L
    }
}