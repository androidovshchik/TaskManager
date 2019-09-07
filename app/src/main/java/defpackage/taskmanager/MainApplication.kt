/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager

import android.app.Application
import android.content.Context
import androidx.room.Room
import defpackage.taskmanager.data.local.AppDatabase
import defpackage.taskmanager.data.models.Signal
import defpackage.taskmanager.extensions.isOreoPlus
import defpackage.taskmanager.extensions.startForegroundService
import defpackage.taskmanager.services.TasksService
import org.jetbrains.anko.notificationManager
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidCoreModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

@Suppress("unused")
class MainApplication : Application(), KodeinAware {

    override val kodein by Kodein.lazy {

        import(androidCoreModule(this@MainApplication))

        bind<Context>() with instance(applicationContext)

        bind<AppDatabase>() with singleton {
            Room.databaseBuilder(instance(), AppDatabase::class.java, "app.db")
                .build()
        }
    }

    override fun onCreate() {
        super.onCreate()
        if (isOreoPlus()) {
            notificationManager.apply {
                Signal.map.forEach { (_, u) ->
                    createNotificationChannel(u.buildChannel())
                }
            }
        }
        startForegroundService<TasksService>()
    }
}