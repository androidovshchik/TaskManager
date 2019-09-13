/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.models

import androidx.room.ColumnInfo

class TaskCount : Task() {

    @ColumnInfo(name = "Count")
    var count = 0

    override fun toString(): String {
        return "TaskCount(count=$count) ${super.toString()}"
    }
}