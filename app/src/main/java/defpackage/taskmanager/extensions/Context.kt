/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

@file:Suppress("unused")

package defpackage.taskmanager.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.*
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.os.SystemClock
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.WorkerThread
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

fun Context.pendingReceiverFor(intent: Intent): PendingIntent =
    PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

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

@WorkerThread
@Throws(IllegalArgumentException::class)
fun Context.getRealPath(uri: Uri): String? {
    return when {
        !isKitkatPlus() -> getDataColumn(uri)
        else -> getRealPathKitkatPlus(uri)
    }
}

@SuppressLint("NewApi")
@Throws(IllegalArgumentException::class)
private fun Context.getRealPathKitkatPlus(uri: Uri): String? {
    when {
        DocumentsContract.isDocumentUri(this, uri) -> when {
            isExternalStorageDocument(uri) -> {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":")
                if ("primary".equals(split[0], true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            }
            isDownloadsDocument(uri) -> {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri =
                    ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), id.toLong())
                return getDataColumn(contentUri)
            }
            isMediaDocument(uri) -> {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":")
                val contentUri = when (split[0].toLowerCase()) {
                    "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    else -> return null
                }
                return getDataColumn(contentUri, "_id=?", arrayOf(split[1]))
            }
        }
        "content".equals(uri.scheme, true) -> return when {
            isGooglePhotosUri(uri) -> uri.lastPathSegment
            else -> getDataColumn(uri)
        }
        "file".equals(uri.scheme, true) -> return uri.path
    }
    return null
}

@Throws(IllegalArgumentException::class)
private fun Context.getDataColumn(uri: Uri, selection: String? = null, selectionArgs: Array<String>? = null): String? {
    val dataColumn = "_data"
    contentResolver?.query(
        uri, arrayOf(
            dataColumn
        ), selection, selectionArgs, null
    )?.use {
        if (it.moveToFirst()) {
            return it.getString(it.getColumnIndexOrThrow(dataColumn))
        }
    }
    return null
}