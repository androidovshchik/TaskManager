/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.local

import androidx.room.*
import defpackage.taskmanager.data.models.Task
import defpackage.taskmanager.data.models.TaskEvents

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

    /**
     * For tasks screen
     */
    @Query("SELECT * FROM Tasks LIMIT 200 OFFSET :offset")
    fun getAllTasksInternal(offset: Long): List<Task>

    /**
     * For tasks service
     */
    @Transaction
    @Query("SELECT * FROM Tasks WHERE `Статус` = 1")
    fun getActiveTasksInternal(): List<TaskEvents>
}