/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.local

import androidx.room.Dao
import androidx.room.Query
import defpackage.taskmanager.data.models.Record

@Dao
interface RecordDao {

    @Query("SELECT * FROM History WHERE `Задача` = :id")
    fun getRecordsByTaskInternal(id: Long): List<Record>
}