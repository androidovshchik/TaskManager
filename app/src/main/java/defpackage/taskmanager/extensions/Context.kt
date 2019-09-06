/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

@file:Suppress("unused")

package defpackage.taskmanager.extensions

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.SystemClock
import androidx.core.content.PermissionChecker.PermissionResult
import org.jetbrains.anko.*

@PermissionResult
fun Context.areGranted(vararg permissions: String): Boolean {
    for (permission in permissions) {
        if (checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
    }
    return true
}

inline fun <reified T : Service> Context.startForegroundService(vararg params: Pair<String, Any?>) {
    if (isOreoPlus()) {
        startForegroundService(intentFor<T>(*params))
    } else {
        startService<T>(*params)
    }
}

inline fun <reified T : Activity> Context.pendingActivityFor(
    flags: Int = PendingIntent.FLAG_UPDATE_CURRENT,
    vararg params: Pair<String, Any?>
): PendingIntent =
    PendingIntent.getActivity(applicationContext, 0, intentFor<T>(*params), flags)

inline fun <reified T : BroadcastReceiver> Context.pendingReceiverFor(
    flags: Int = PendingIntent.FLAG_UPDATE_CURRENT,
    vararg params: Pair<String, Any?>
): PendingIntent =
    PendingIntent.getBroadcast(applicationContext, 0, intentFor<T>(*params), flags)

inline fun <reified T : BroadcastReceiver> Context.pendingReceiverFor(
    action: String,
    flags: Int = PendingIntent.FLAG_UPDATE_CURRENT
): PendingIntent =
    PendingIntent.getBroadcast(applicationContext, 0, Intent(action), flags)

inline fun <reified T : BroadcastReceiver> Context.createAlarm(interval: Int) {
    cancelAlarm<T>()
    when {
        isMarshmallowPlus() -> alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + interval, pendingReceiverFor<T>()
        )
        isKitkatPlus() -> alarmManager.setExact(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + interval, pendingReceiverFor<T>()
        )
        else -> alarmManager.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + interval, pendingReceiverFor<T>()
        )
    }
}

inline fun <reified T : BroadcastReceiver> Context.cancelAlarm() {
    alarmManager.cancel(pendingReceiverFor<T>())
}