/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import defpackage.taskmanager.data.models.Event
import defpackage.taskmanager.data.models.Signal
import defpackage.taskmanager.data.models.Task
import defpackage.taskmanager.data.models.Week

@Database(
    entities = [
        Task::class,
        Event::class,
        Week::class,
        Signal::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    abstract fun eventDao(): EventDao
}