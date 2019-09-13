/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.receivers

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import defpackage.taskmanager.EXTRA_NONE
import defpackage.taskmanager.data.local.Preferences
import defpackage.taskmanager.services.TasksService

class RebootReceiver : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        TasksService.launch(
            Preferences(context),
            EXTRA_NONE to true
        )
    }
}