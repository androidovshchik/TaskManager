/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Events",
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
    var status: Int? = null

    @ColumnInfo(name = "Время")
    var time: Int? = null
}
