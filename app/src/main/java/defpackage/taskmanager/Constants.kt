/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager

import android.Manifest

val DANGER_PERMISSIONS = arrayOf(
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)

const val EXTRA_TASK = "extra_task"

const val EXTRA_RESULT = "extra_result"

const val EXTRA_RECEIVER = "extra_receiver"