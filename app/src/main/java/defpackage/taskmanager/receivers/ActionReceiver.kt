/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import defpackage.taskmanager.ACTION_UNKNOWN
import defpackage.taskmanager.EXTRA_ACTION
import defpackage.taskmanager.EXTRA_TASK
import defpackage.taskmanager.data.local.Preferences
import defpackage.taskmanager.services.TasksService

class ActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        TasksService.launch(
            Preferences(context),
            EXTRA_TASK to intent.getLongExtra(EXTRA_TASK, 0L),
            EXTRA_ACTION to intent.getIntExtra(EXTRA_ACTION, ACTION_UNKNOWN)
        )
    }
}