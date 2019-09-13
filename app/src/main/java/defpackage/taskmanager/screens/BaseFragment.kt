/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

@file:Suppress("DEPRECATION")

package defpackage.taskmanager.screens

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.view.View
import com.elvishew.xlog.XLog
import defpackage.taskmanager.data.local.Preferences
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

open class BaseFragment : Fragment(), KodeinAware, CoroutineScope {

    override val kodein by kodein()

    protected var preferences: Preferences? = null

    protected val fragmentJob = SupervisorJob()

    protected val appContext: Context?
        get() = activity?.applicationContext

    protected val args: Bundle
        get() = arguments ?: Bundle()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appContext?.let {
            preferences = Preferences(it)
        }
    }

    override fun onDestroyView() {
        fragmentJob.cancelChildren()
        super.onDestroyView()
    }

    override val coroutineContext = Dispatchers.Main + fragmentJob + CoroutineExceptionHandler { _, e ->
        XLog.e(e.localizedMessage, e)
    }
}