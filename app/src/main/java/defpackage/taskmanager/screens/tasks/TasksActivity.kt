/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.tasks

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.TextView
import defpackage.taskmanager.DANGER_PERMISSIONS
import defpackage.taskmanager.data.local.DbManager
import defpackage.taskmanager.data.local.Preferences
import defpackage.taskmanager.extensions.areGranted
import defpackage.taskmanager.extensions.getRealPath
import defpackage.taskmanager.extensions.requestPermissions
import defpackage.taskmanager.screens.BaseActivity
import defpackage.taskmanager.services.TasksManager
import defpackage.taskmanager.services.TasksService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.setContentView
import org.kodein.di.generic.instance

class TasksActivity : BaseActivity() {

    val preferences: Preferences by instance()

    val dbManager: DbManager by instance()

    val tasksManager: TasksManager by instance()

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
        if (!areGranted(*DANGER_PERMISSIONS)) {
            requestPermissions(REQUEST_PERMISSIONS, *DANGER_PERMISSIONS)
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
        startActivityForResult(Intent.createChooser(Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/*"
        }, "Выберите приложение"), REQUEST_CHOOSE_FILE)
    }

    fun onLoadTasksFromDbFile() {
        dbManager.importDb(applicationContext, preferences.pathToDb)
    }

    fun onLaunchTasksService() {
        Preferences.enableTasksService = true
        bindTasksService()
    }

    fun onStopAllTasks() {
        Preferences.enableTasksService = false
        unbindTasksService()
        TasksService.kill(applicationContext)
    }

    override fun onChanged(t: Boolean) {

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 5000) {
            if (resultCode != RESULT_OK) {
                return
            }
            val uri = data?.data ?: return
            launch {
                val path = withContext(Dispatchers.IO) {
                    getRealPath(uri)
                } ?: uri.path
                Timber.d("local file path: $path")
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!areGranted(*DANGER_PERMISSIONS)) {
            finish()
        }
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

        const val REQUEST_CHOOSE_FILE = 200
    }
}
