/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Intent myIntent = new Intent(context, SmsService.class)
        myIntent.putExtra("msg", msgs)
        context.startService(myIntent)
        context.startForegroundService()
    }
}