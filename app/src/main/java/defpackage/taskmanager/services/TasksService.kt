/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

class TasksService : Service() {

    private val binder = Binder()

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onUnbind(intent: Intent): Boolean {
        return true
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    @Suppress("unused")
    inner class Binder : android.os.Binder() {

        val service: TasksService get() = this@TasksService
    }
}