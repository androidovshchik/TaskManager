/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager

import android.app.Application
import android.os.Environment
import com.chibatching.kotpref.Kotpref
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.backup.NeverBackupStrategy
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator
import defpackage.taskmanager.data.local.DbManager
import defpackage.taskmanager.data.models.Behavior
import defpackage.taskmanager.extensions.isOreoPlus
import defpackage.taskmanager.services.TasksManager
import org.jetbrains.anko.notificationManager
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import java.io.File

@Suppress("unused")
class MainApplication : Application(), KodeinAware {

    override val kodein = Kodein {

        bind<DbManager>() with singleton { DbManager() }

        bind<TasksManager>() with singleton { TasksManager() }
    }

    override fun onCreate() {
        super.onCreate()
        initLogger()
        Kotpref.init(applicationContext)
        if (isOreoPlus()) {
            notificationManager.apply {
                Behavior.map.forEach { (_, u) ->
                    createNotificationChannel(u.buildChannel())
                }
            }
        }
    }

    private fun initLogger() {
        val config = LogConfiguration.Builder()
            .logLevel(LogLevel.ALL)
            .build()
        if (BuildConfig.DEBUG) {
            val documents = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            val filePrinter = FilePrinter.Builder(File(documents, LOGS_FOLDER).path)
                .fileNameGenerator(DateFileNameGenerator())
                .backupStrategy(NeverBackupStrategy())
                .build()
            XLog.init(config, AndroidPrinter(), filePrinter)
        } else {
            XLog.init(config, AndroidPrinter())
        }
    }
}