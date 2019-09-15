/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.local

import androidx.room.*
import defpackage.taskmanager.data.models.Task
import defpackage.taskmanager.data.models.TaskCount

@Dao
interface TaskDao {

    @Transaction
    @Insert
    fun insert(vararg items: Task)

    @Transaction
    @Update
    fun update(vararg items: Task)

    @Transaction
    @Delete
    fun delete(vararg items: Task)

    @Query("SELECT * FROM Tasks")
    fun getAllTasksInternal(): List<TaskCount>

    @Query("SELECT * FROM Tasks WHERE `Статус` = 1")
    fun getActiveTasksInternal(): List<TaskCount>
}