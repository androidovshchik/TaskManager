/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

@file:Suppress("unused")

package defpackage.taskmanager.extensions

import android.net.Uri
import android.os.Build
import android.os.Looper

val isMainThread: Boolean
    get() = Looper.myLooper() == Looper.getMainLooper()

internal fun isExternalStorageDocument(uri: Uri) = "com.android.externalstorage.documents" == uri.authority

internal fun isDownloadsDocument(uri: Uri) = "com.android.providers.downloads.documents" == uri.authority

internal fun isMediaDocument(uri: Uri) = "com.android.providers.media.documents" == uri.authority

internal fun isGooglePhotosUri(uri: Uri) = "com.google.android.apps.photos.content" == uri.authority

fun isKitkat() = Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT

fun isKitkatWatch() = Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT_WATCH

fun isLollipop() = Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP

fun isLollipopMR1() = Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1

fun isMarshmallow() = Build.VERSION.SDK_INT == Build.VERSION_CODES.M

fun isNougat() = Build.VERSION.SDK_INT == Build.VERSION_CODES.N

fun isNougatMR1() = Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1

fun isOreo() = Build.VERSION.SDK_INT == Build.VERSION_CODES.O

fun isOreoMR1() = Build.VERSION.SDK_INT == Build.VERSION_CODES.O_MR1

fun isPie() = Build.VERSION.SDK_INT == Build.VERSION_CODES.P

fun isKitkatPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

fun isKitkatWatchPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH

fun isLollipopPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

fun isLollipopMR1Plus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1

fun isMarshmallowPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

fun isNougatPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

fun isNougatMR1Plus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1

fun isOreoPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

fun isOreoMR1Plus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1

fun isPiePlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P