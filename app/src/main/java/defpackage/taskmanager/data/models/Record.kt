/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
class Record {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    var id = 0L

    @ColumnInfo(name = "Задача", index = true)
    var task = 0L

    @ColumnInfo(name = "Статус")
    var status: Boolean? = null

    @ColumnInfo(name = "Время")
    var time: String? = null

    companion object {

        const val STATUS_NONE = 0

        const val STATUS_COMPLETED = 10

        const val STATUS_DEFERRED = 20

        const val STATUS_CANCELLED = 30
    }
}
