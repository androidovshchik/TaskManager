/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.services

import defpackage.taskmanager.data.models.Task

class TasksManager {

    private val tasks = arrayListOf<Task>()

    fun setTasks() {
        tasks.apply {
            clear()
        }
    }

    fun onTaskChange() {

    }

    fun nextTask() {

    }

    fun recreateAlarms() {

    }

    @Synchronized
    fun getTasks() {

    }
}