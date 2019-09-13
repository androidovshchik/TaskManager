/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import com.elvishew.xlog.XLog
import defpackage.taskmanager.data.local.Preferences
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

@SuppressLint("Registered")
open class BaseActivity : Activity(), KodeinAware, CoroutineScope {

    override val kodein by kodein()

    protected lateinit var preferences: Preferences

    protected val activityJob = SupervisorJob()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = Preferences(applicationContext)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        activityJob.cancelChildren()
        super.onDestroy()
    }

    override val coroutineContext = Dispatchers.Main + activityJob + CoroutineExceptionHandler { _, e ->
        XLog.e(e.localizedMessage, e)
    }
}
