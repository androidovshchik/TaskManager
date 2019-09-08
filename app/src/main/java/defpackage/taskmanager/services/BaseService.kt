/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.services

import android.app.Service
import com.elvishew.xlog.XLog
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

abstract class BaseService : Service(), KodeinAware, CoroutineScope {

    override val kodein by kodein()

    val serviceJob = SupervisorJob()

    override fun onDestroy() {
        serviceJob.cancelChildren()
        super.onDestroy()
    }

    override val coroutineContext = Dispatchers.Main + serviceJob + CoroutineExceptionHandler { _, e ->
        XLog.e(e.localizedMessage, e)
    }
}
