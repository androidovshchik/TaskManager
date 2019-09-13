/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import defpackage.taskmanager.EXTRA_STATUS
import defpackage.taskmanager.EXTRA_TASK
import defpackage.taskmanager.data.local.Preferences
import defpackage.taskmanager.data.models.Record
import defpackage.taskmanager.services.TasksService

class ActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        TasksService.launch(
            Preferences(context),
            EXTRA_TASK to intent.getLongExtra(EXTRA_TASK, 0L),
            EXTRA_STATUS to intent.getIntExtra(EXTRA_STATUS, Record.STATUS_NONE)
        )
    }
}