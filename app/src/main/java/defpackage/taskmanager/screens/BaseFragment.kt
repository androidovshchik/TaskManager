/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

@file:Suppress("DEPRECATION")

package defpackage.taskmanager.screens

import android.app.Fragment
import android.os.Bundle
import com.elvishew.xlog.XLog
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

open class BaseFragment : Fragment(), KodeinAware, CoroutineScope {

    override val kodein by kodein()

    val fragmentJob = SupervisorJob()

    protected val args: Bundle
        get() = arguments ?: Bundle()

    override fun onDestroyView() {
        fragmentJob.cancelChildren()
        super.onDestroyView()
    }

    override val coroutineContext = Dispatchers.Main + fragmentJob + CoroutineExceptionHandler { _, e ->
        XLog.e(e.localizedMessage, e)
    }
}