/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.tasks

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.TextView
import defpackage.taskmanager.data.local.Preferences
import defpackage.taskmanager.extensions.areGranted
import defpackage.taskmanager.extensions.requestPermissions
import defpackage.taskmanager.screens.BaseActivity
import defpackage.taskmanager.services.TasksService
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.setContentView

class TasksActivity : BaseActivity() {

    lateinit var tvInfo: TextView

    lateinit var tvStatus: TextView

    private var tasksService: TasksService? = null

    private val tasksFragment = TasksFragment()

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TasksActivityUI().setContentView(this)
        fragmentManager.beginTransaction()
            .add(TasksActivityUI.FRAME_LAYOUT_ID, tasksFragment)
            .commit()
        if (!areGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            requestPermissions(REQUEST_PERMISSIONS, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    override fun onStart() {
        super.onStart()
        bindTasksService()
    }

    private fun bindTasksService() {
        if (TasksService.launch(applicationContext)) {
            bindService(intentFor<TasksService>(), tasksConnection, Context.BIND_AUTO_CREATE)
        }
    }

    fun onChooseDbFile() {

    }

    fun onLoadTasksFromDbFile() {

    }

    fun onLaunchTasksService() {
        Preferences.enabledTasksService = true
        if (TasksService.launch(applicationContext)) {
            bindService(intentFor<TasksService>(), tasksConnection, Context.BIND_AUTO_CREATE)
        }
    }

    fun onStopAllTasks() {
        Preferences.enabledTasksService = false
        unbindTasksService()
        TasksService.kill(applicationContext)
    }

    private fun unbindTasksService() {
        if (tasksService != null) {
            unbindService(tasksConnection)
            tasksService = null
        }
    }

    override fun onStop() {
        unbindTasksService()
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

    companion object {

        const val REQUEST_PERMISSIONS = 100
    }
}
