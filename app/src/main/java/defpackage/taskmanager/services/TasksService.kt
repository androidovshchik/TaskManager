/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.services

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.elvishew.xlog.XLog
import defpackage.taskmanager.R
import defpackage.taskmanager.data.local.Preferences
import defpackage.taskmanager.data.models.Behavior
import defpackage.taskmanager.extensions.isRunning
import defpackage.taskmanager.extensions.pendingActivityFor
import defpackage.taskmanager.extensions.startForegroundService
import defpackage.taskmanager.screens.tasks.TasksActivity
import kotlinx.coroutines.Job
import org.jetbrains.anko.*
import org.joda.time.DateTimeZone
import java.util.*

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
        registerReceiver(receiver, IntentFilter().apply {
            addAction(Intent.ACTION_TIMEZONE_CHANGED)
        })
    }

    @SuppressLint("WakelockTimeout")
    private fun acquireWakeLock() {
        if (wakeLock == null) {
            wakeLock =
                powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, javaClass.name).apply {
                    acquire()
                }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun releaseWakeLock() {
        wakeLock?.let {
            it.release()
            wakeLock = null
        }
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        releaseWakeLock()
        super.onDestroy()
    }

    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            try {
                val timeZone = intent.getStringExtra("time-zone")
                DateTimeZone.setDefault(DateTimeZone.forTimeZone(TimeZone.getDefault()))
                XLog.d("TIMEZONE_CHANGED received, changed default timezone to \"$timeZone\"")
            } catch (e: Exception) {
                XLog.e(e.localizedMessage, e)
            }
        }
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
        fun kill(context: Context): Boolean = context.run {
            notificationManager.cancelAll()
            return if (activityManager.isRunning<TasksService>()) {
                stopService<TasksService>()
            } else {
                true
            }
        }
    }
}