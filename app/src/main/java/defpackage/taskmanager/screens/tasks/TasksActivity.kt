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
import android.widget.EditText
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import defpackage.taskmanager.DANGER_PERMISSIONS
import defpackage.taskmanager.EXTRA_ID
import defpackage.taskmanager.data.local.DbManager
import defpackage.taskmanager.extensions.areGranted
import defpackage.taskmanager.extensions.getRealPath
import defpackage.taskmanager.extensions.requestPermissions
import defpackage.taskmanager.extensions.setTextSelection
import defpackage.taskmanager.screens.BaseActivity
import defpackage.taskmanager.screens.events.EventsFragment
import defpackage.taskmanager.services.TasksService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.setContentView
import org.kodein.di.generic.instance

@Suppress("DEPRECATION")
class TasksActivity : BaseActivity() {

    val dbManager: DbManager by instance()

    lateinit var etDbPath: EditText

    lateinit var vpLists: ViewPager

    lateinit var tvStatus: TextView

    private var tasksService: TasksService? = null

    private lateinit var pagerAdapter: TasksPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TasksActivityUI().setContentView(this)
        etDbPath.setTextSelection(preferences.pathToDb)
        pagerAdapter = TasksPagerAdapter(fragmentManager).apply {
            val id = intent.getLongExtra(EXTRA_ID, 0L)
            fragments.put(0, EventsFragment.newInstance(id))
            fragments.put(1, TasksFragment.newInstance(id))
        }
        vpLists.adapter = pagerAdapter
        if (!areGranted(*DANGER_PERMISSIONS)) {
            requestPermissions(REQUEST_PERMISSIONS, *DANGER_PERMISSIONS)
        }
    }

    override fun onStart() {
        super.onStart()
        bindTasksService()
    }

    private fun bindTasksService() {
        if (TasksService.launch(preferences)) {
            if (tasksService == null) {
                bindService(intentFor<TasksService>(), tasksConnection, Context.BIND_AUTO_CREATE)
            }
        }
    }

    fun onChooseDbFile() {
        if (areGranted(*DANGER_PERMISSIONS)) {
            startActivityForResult(Intent.createChooser(Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*/*"
            }, "Выберите приложение"), REQUEST_CHOOSE_FILE)
        }
    }

    fun onLoadDbFile() {
        if (areGranted(*DANGER_PERMISSIONS)) {
            for (i in 0 until pagerAdapter.count) {
                pagerAdapter.getItem(i)
                    .clearData()
            }
            onKillTasks()
            dbManager.importDb(preferences, etDbPath.text.toString().trim())
        }
    }

    fun onSaveDbFile() {
        if (areGranted(*DANGER_PERMISSIONS)) {
            dbManager.exportDb(preferences)
        }
    }

    fun onLaunchTasks() {
        preferences.enableTasksService = true
        bindTasksService()
    }

    fun onKillTasks() {
        preferences.enableTasksService = false
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHOOSE_FILE) {
            if (resultCode != RESULT_OK) {
                return
            }
            val uri = data?.data ?: return
            launch {
                val path = withContext(Dispatchers.IO) {
                    getRealPath(uri) ?: uri.path
                }
                etDbPath.setTextSelection(path)
            }
        }
    }

    private val tasksConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            tasksService = (binder as TasksService.Binder).service
            tvStatus.text = "Статус: работает"
        }

        override fun onServiceDisconnected(name: ComponentName) {
            tasksService = null
            tvStatus.text = "Статус: не работает"
        }
    }

    companion object {

        private const val REQUEST_PERMISSIONS = 100

        private const val REQUEST_CHOOSE_FILE = 200
    }
}
