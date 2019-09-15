/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.models

import androidx.room.ColumnInfo
import androidx.room.Embedded

class EventTask {

    @Embedded
    lateinit var event: Event

    @ColumnInfo(name = "_TITLE")
    lateinit var title: String
}
