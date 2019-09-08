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
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.os.SystemClock
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.PermissionChecker.PermissionResult
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import defpackage.taskmanager.receivers.ToastReceiver
import org.jetbrains.anko.alarmManager
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startService

fun Context.createXmlDrawable(@DrawableRes id: Int): VectorDrawableCompat? {
    return if (id != 0) {
        VectorDrawableCompat.create(resources, id, theme)
    } else null
}

fun Context.bgToast(message: String) = sendBroadcast(intentFor<ToastReceiver>().apply {
    putExtra(ToastReceiver.EXTRA_MESSAGE, message)
    putExtra(ToastReceiver.EXTRA_DURATION, Toast.LENGTH_SHORT)
})

fun Context.longBgToast(message: String) = sendBroadcast(intentFor<ToastReceiver>().apply {
    putExtra(ToastReceiver.EXTRA_MESSAGE, message)
    putExtra(ToastReceiver.EXTRA_DURATION, Toast.LENGTH_LONG)
})

@PermissionResult
fun Context.areGranted(vararg permissions: String): Boolean {
    for (permission in permissions) {
        if (checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
    }
    return true
}

inline fun <reified T : Service> Context.startForegroundService(vararg params: Pair<String, Any?>): ComponentName? {
    return if (isOreoPlus()) {
        startForegroundService(intentFor<T>(*params))
    } else {
        startService<T>(*params)
    }
}

inline fun <reified T : Activity> Context.pendingActivityFor(
    vararg params: Pair<String, Any?>
): PendingIntent =
    PendingIntent.getActivity(applicationContext, 0, intentFor<T>(*params), PendingIntent.FLAG_UPDATE_CURRENT)

inline fun <reified T : BroadcastReceiver> Context.pendingReceiverFor(
    vararg params: Pair<String, Any?>
): PendingIntent =
    PendingIntent.getBroadcast(applicationContext, 0, intentFor<T>(*params), PendingIntent.FLAG_UPDATE_CURRENT)

inline fun <reified T : BroadcastReceiver> Context.pendingReceiverFor(
    action: String
): PendingIntent =
    PendingIntent.getBroadcast(applicationContext, 0, Intent(action), PendingIntent.FLAG_UPDATE_CURRENT)

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

fun Context.scanFile(path: String) {
    sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).apply {
        data = path.toFileUri()
    })
    MediaScannerConnection.scanFile(applicationContext, arrayOf(path), null, null)
}