/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import defpackage.taskmanager.data.models.Task
import defpackage.taskmanager.extensions.isOreoPlus

class TasksManager {

    private val tasks = arrayListOf<Task>()

    fun setTasks() {
        tasks.apply {
            clear()
            addAll()
        }
    }

    fun onTaskChange() {

    }

    fun nextTask() {

    }

    fun recreateAlarms() {

    }
}