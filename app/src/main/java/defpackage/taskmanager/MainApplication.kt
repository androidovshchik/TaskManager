/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.chibatching.kotpref.Kotpref
import defpackage.taskmanager.data.local.AppDatabase
import defpackage.taskmanager.data.models.Record
import defpackage.taskmanager.data.models.Signal
import defpackage.taskmanager.data.models.Task
import defpackage.taskmanager.extensions.isOreoPlus
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

        bind<List<Task>>() with singleton { arrayListOf<Task>() }

        bind<List<Record>>() with singleton { arrayListOf<Record>() }

        bind<AppDatabase>() with singleton {
            Room.databaseBuilder(instance(), AppDatabase::class.java, "app.db")
                .build()
        }
    }

    override fun onCreate() {
        super.onCreate()
        Kotpref.init(applicationContext)
        if (isOreoPlus()) {
            notificationManager.apply {
                Signal.map.forEach { (_, u) ->
                    createNotificationChannel(u.buildChannel())
                }
            }
        }
    }
}