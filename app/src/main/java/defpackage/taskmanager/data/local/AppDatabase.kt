/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import defpackage.taskmanager.data.models.*

@Database(
    entities = [
        Task::class,
        Record::class,
        Event::class,
        Week::class,
        Signal::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    abstract fun recordDao(): RecordDao

    abstract fun eventDao(): EventDao
}