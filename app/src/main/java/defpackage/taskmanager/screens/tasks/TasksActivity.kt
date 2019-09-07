/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.tasks

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.recyclerview.widget.RecyclerView
import defpackage.taskmanager.screens.base.BaseActivity
import defpackage.taskmanager.services.TasksService
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.setContentView

class TasksActivity : BaseActivity() {

    lateinit var recyclerView: RecyclerView

    private var tasksService: TasksService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TasksActivityUI().setContentView(this)
    }

    override fun onStart() {
        super.onStart()
        if (TasksService.start(applicationContext) != null) {
            bindService(intentFor<TasksService>(), serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    fun onChooseDbFile() {

    }

    fun onLoadTasksFromDbFile() {

    }

    override fun onStop() {
        if (tasksService != null) {
            unbindService(serviceConnection)
            tasksService = null
        }
        super.onStop()
    }

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName) {
            tasksService = null
        }

        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            tasksService = (binder as TasksService.Binder).service
        }
    }
}
