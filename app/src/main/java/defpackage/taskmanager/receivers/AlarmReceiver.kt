/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import defpackage.taskmanager.services.TasksService

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        TasksService.launch(context)
    }
}