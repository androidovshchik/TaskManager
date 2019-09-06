/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import defpackage.taskmanager.extensions.startForegroundService

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.startForegroundService<TasksService>()
    }
}