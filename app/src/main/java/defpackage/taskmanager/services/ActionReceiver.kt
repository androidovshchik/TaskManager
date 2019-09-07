/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import defpackage.taskmanager.EXTRA_RESULT
import defpackage.taskmanager.EXTRA_TASK
import defpackage.taskmanager.extensions.startForegroundService

class ActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.startForegroundService<TasksService>(
            EXTRA_TASK to intent.getLongExtra(EXTRA_TASK, 0L),
            EXTRA_RESULT to intent.getBooleanExtra(EXTRA_RESULT, null)
        )
    }
}