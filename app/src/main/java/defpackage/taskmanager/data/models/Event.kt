/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import defpackage.taskmanager.extensions.currentTimeSeconds

@Entity(
    tableName = "History",
    foreignKeys = [
        ForeignKey(
            entity = Task::class,
            parentColumns = ["ID"],
            childColumns = ["Задача"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class Event {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    var id = 0L

    @ColumnInfo(name = "Задача", index = true)
    var task = 0L

    @ColumnInfo(name = "Статус")
    var status: Boolean? = null

    @ColumnInfo(name = "Время")
    var time: String? = null

    /**
     * Initial time of event which may be compared
     */
    @ColumnInfo(name = "_EVENT_TIME")
    var eventTime: Long? = null

    /**
     * Delayed time of event
     */
    @ColumnInfo(name = "_DEFER_TIME")
    var deferTime: Long? = null

    val nextTime: Long?
        get() {

        }

    val willBeInFuture: Boolean
        get() = eventTime == null || currentTimeSeconds() < eventTime!!

    val description: String
        get() = when (status) {
            true -> "выполнено"
            false -> "отменено"
            else -> "неизвестно"
        }

    override fun toString(): String {
        return "Event(id=$id, task=$task, status=$status, time=$time, eventTime=$eventTime, deferTime=$deferTime)"
    }
}
