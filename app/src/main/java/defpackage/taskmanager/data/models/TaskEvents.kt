/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.models

import androidx.room.Embedded
import androidx.room.Relation

class TaskEvents {

    @Embedded
    lateinit var task: Task

    @Relation(parentColumn = "ID", entityColumn = "Задача", entity = Event::class)
    lateinit var events: List<Event>
}
