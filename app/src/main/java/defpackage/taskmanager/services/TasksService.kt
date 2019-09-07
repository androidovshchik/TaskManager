/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import defpackage.taskmanager.EXTRA_RESULT
import defpackage.taskmanager.EXTRA_TASK
import defpackage.taskmanager.R
import defpackage.taskmanager.data.models.Signal
import defpackage.taskmanager.extensions.isRunning
import defpackage.taskmanager.extensions.pendingActivityFor
import defpackage.taskmanager.extensions.startForegroundService
import defpackage.taskmanager.screens.tasks.TasksActivity
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.anko.activityManager
import org.jetbrains.anko.startService
import kotlin.coroutines.CoroutineContext

class TasksService : Service(), CoroutineScope {

    private val tasksManager = TasksManager()

    private val binder = Binder()

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onUnbind(intent: Intent): Boolean {
        return true
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(
            0, NotificationCompat.Builder(applicationContext, Signal.SOUNDLESS.name)
                .setSmallIcon(R.drawable.ic_notifications_white_24dp)
                .setContentTitle("")
                .setContentIntent(pendingActivityFor<TasksActivity>())
                .setOngoing(true)
                .build()
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            if (it.hasExtra(EXTRA_TASK) && it.hasExtra(EXTRA_RESULT)) {

            }
        }
        return START_STICKY
    }

    override val coroutineContext: CoroutineContext
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    @Suppress("unused")
    inner class Binder : android.os.Binder() {

        val service: TasksService
            get() = this@TasksService
    }

    companion object {

        fun start(context: Context, vararg params: Pair<String, Any?>) {
            context.run {
                if (!activityManager.isRunning<TasksService>()) {
                    try {
                        startForegroundService<TasksService>(*params)
                    } catch (e: SecurityException) {
                    }
                } else {
                    context.startService<TasksService>(*params)
                }
            }
        }
    }
}