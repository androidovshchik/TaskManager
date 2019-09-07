/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.tasks

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import defpackage.taskmanager.screens.base.BaseActivity
import defpackage.taskmanager.services.TasksService
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.setContentView

class TasksActivity : BaseActivity() {

    private var tasksService: TasksService? = null

    private lateinit var tasksFragment: TasksFragment

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tasksFragment = TasksFragment()
        TasksActivityUI().setContentView(this)
        fragmentManager.beginTransaction()
            .add(TasksActivityUI.FRAME_LAYOUT_ID, tasksFragment)
            .commit()
    }

    override fun onStart() {
        super.onStart()
        if (TasksService.start(applicationContext) != null) {
            bindService(intentFor<TasksService>(), tasksConnection, Context.BIND_AUTO_CREATE)
        }
    }

    fun onChooseDbFile() {

    }

    fun onLoadTasksFromDbFile() {

    }

    override fun onStop() {
        if (tasksService != null) {
            unbindService(tasksConnection)
            tasksService = null
        }
        super.onStop()
    }

    private val tasksConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            tasksService = (binder as TasksService.Binder).service
        }

        override fun onServiceDisconnected(name: ComponentName) {
            tasksService = null
        }
    }
}
