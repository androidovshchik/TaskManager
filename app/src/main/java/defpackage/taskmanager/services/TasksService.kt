/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import defpackage.taskmanager.EXTRA_RESULT
import defpackage.taskmanager.EXTRA_TASK
import kotlinx.coroutines.CoroutineScope
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
}