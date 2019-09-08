/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.local

import androidx.room.Dao
import androidx.room.Query
import defpackage.taskmanager.data.models.TaskCount

@Dao
interface TaskDao {

    @Query("SELECT Tasks.*, COUNT(History.ID) as Count FROM Tasks LEFT JOIN History ON Tasks.ID = History.`Задача`")
    fun getAllTasks(): List<TaskCount>
}