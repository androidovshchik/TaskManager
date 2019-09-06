/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.tasks

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import defpackage.taskmanager.screens.base.BaseActivity
import org.jetbrains.anko.setContentView

class TasksActivity : BaseActivity() {

    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TasksActivityUI().setContentView(this)
    }

    override fun onStart() {
        super.onStart()
        if (!isServiceRunning(SoundService::class.java)) {
            startService(newIntent(SoundService::class.java))
        }
        bindService(newIntent(SoundService::class.java), serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        service?.stopPlay()
        if (service != null) {
            unbindService(serviceConnection)
            service = null
        }
    }

    fun onChooseDbFile() {

    }

    fun onLoadTasksFromDbFile() {

    }
}
