/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.local

import androidx.room.*
import defpackage.taskmanager.data.models.Event
import defpackage.taskmanager.data.models.EventTask

@Dao
interface EventDao {

    @Transaction
    @Insert
    fun insert(vararg items: Event)

    @Transaction
    @Update
    fun update(vararg items: Event)

    /**
     * For tasks screen and widgets
     */
    @Transaction
    @Query("SELECT History.*, Tasks.`Название` AS _TITLE FROM History LEFT JOIN Tasks ON History.`Задача` = Tasks.ID ORDER BY History._EVENT_TIME DESC LIMIT 200 OFFSET :offset")
    fun getAllEvents(offset: Long): List<EventTask>

    /**
     * For history screen
     */
    @Query("SELECT * FROM History WHERE `Задача` = :id ORDER BY _EVENT_TIME ASC LIMIT 200 OFFSET :offset")
    fun getEventsByTask(id: Long, offset: Long): List<Event>
}