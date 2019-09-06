/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.room.Room
import defpackage.taskmanager.data.local.AppDatabase
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

        bind<AppDatabase>() with singleton {
            Room.databaseBuilder(instance(), AppDatabase::class.java, "app.db")
                .build()
        }
    }

    override fun onCreate() {
        super.onCreate()
        if (isOreoPlus()) {
            notificationManager.apply {
                createNotificationChannel(
                    NotificationChannel(
                        SILENT_CHANNEL_ID, SILENT_CHANNEL_ID,
                        NotificationManager.IMPORTANCE_LOW
                    ).apply {
                        lockscreenVisibility = Notification.VISIBILITY_SECRET
                    })
                createNotificationChannel(
                    NotificationChannel(
                        NOISY_CHANNEL_ID, NOISY_CHANNEL_ID,
                        NotificationManager.IMPORTANCE_HIGH
                    ).apply {
                        enableLights(true)
                        enableVibration(true)
                        lightColor = color
                        vibrationPattern = longArrayOf(1000, 1000)
                        lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                    })
            }
        }
    }

    companion object {

        const val SILENT_CHANNEL_ID = "app_silent_channel"

        const val NOISY_CHANNEL_ID = "app_noisy_channel"
    }
}