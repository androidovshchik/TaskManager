/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.view.MenuItem
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinTrigger
import org.kodein.di.android.kodein
import timber.log.Timber

@SuppressLint("Registered")
open class BaseActivity : Activity(), KodeinAware, CoroutineScope {

    override val kodein by kodein()

    override val kodeinTrigger = KodeinTrigger()

    val activityJob = SupervisorJob()

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
        Timber.e(e)
    }
}
