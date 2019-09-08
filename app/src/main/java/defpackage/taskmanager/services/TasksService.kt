/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.services

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import defpackage.taskmanager.R
import defpackage.taskmanager.data.local.Preferences
import defpackage.taskmanager.data.models.Behavior
import defpackage.taskmanager.extensions.isRunning
import defpackage.taskmanager.extensions.pendingActivityFor
import defpackage.taskmanager.extensions.startForegroundService
import defpackage.taskmanager.screens.tasks.TasksActivity
import kotlinx.coroutines.Job
import org.jetbrains.anko.activityManager
import org.jetbrains.anko.powerManager
import org.jetbrains.anko.startService
import org.jetbrains.anko.stopService

class TasksService : BaseService() {

    private val binder = Binder()

    private var job: Job? = null

    private var wakeLock: PowerManager.WakeLock? = null

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onUnbind(intent: Intent): Boolean {
        return true
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(
            Int.MAX_VALUE, NotificationCompat.Builder(applicationContext, Behavior.SOUNDLESS.name)
                .setSmallIcon(R.drawable.ic_schedule_white_24dp)
                .setContentTitle("Фоновой сервис")
                .setContentIntent(pendingActivityFor<TasksActivity>())
                .setOngoing(true)
                .build()
        )
    }

    @SuppressLint("WakelockTimeout")
    private fun acquireWakeLock() {
        if (wakeLock == null) {
            wakeLock =
                powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "$packageName:${javaClass.simpleName}").apply {
                    acquire()
                }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        /*job?.let {
            it.cancel()
            job = null
        }
        executeJob()*/
        return START_STICKY
    }

    /*fun executeJob() {
        job = launch {
            try {
                acquireWakeLock()
                intent.let {
                    if (it.hasExtra(EXTRA_TASK) && it.hasExtra(EXTRA_RESULT)) {

                    }
                }
            } finally {
                releaseWakeLock()
            }
        }
    }*/

    private fun releaseWakeLock() {
        wakeLock?.let {
            it.release()
            wakeLock = null
        }
    }

    override fun onDestroy() {
        releaseWakeLock()
        super.onDestroy()
    }

    @Suppress("unused")
    inner class Binder : android.os.Binder() {

        val service: TasksService
            get() = this@TasksService
    }

    companion object {

        /**
         * @param params might not be empty
         * @return true if service is running
         */
        @JvmStatic
        fun launch(preferences: Preferences, vararg params: Pair<String, Any?>): Boolean = preferences.run {
            return if (enableTasksService) {
                if (context.activityManager.isRunning<TasksService>()) {
                    if (params.isNotEmpty()) {
                        context.startService<TasksService>(*params) != null
                    } else {
                        true
                    }
                } else {
                    try {
                        context.startForegroundService<TasksService>(*params) != null
                    } catch (e: SecurityException) {
                        false
                    }
                }
            } else {
                !kill(context)
            }
        }

        /**
         * @return true if service is stopped
         */
        @JvmStatic
        fun kill(context: Context): Boolean = context.run {
            return if (activityManager.isRunning<TasksService>()) {
                stopService<TasksService>()
            } else {
                true
            }
        }
    }
}