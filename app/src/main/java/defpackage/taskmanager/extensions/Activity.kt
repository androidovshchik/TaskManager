/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

@file:Suppress("unused")

package defpackage.taskmanager.extensions

import android.app.Activity

fun Activity.requestPermissions(requestCode: Int, vararg permissions: String = allAppPermissions) {
    if (isMarshmallowPlus()) {
        requestPermissions(permissions, requestCode)
    }
}