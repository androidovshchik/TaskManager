/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "History")
class Record : Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    var id = 0L

    @ColumnInfo(name = "Задача")
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
