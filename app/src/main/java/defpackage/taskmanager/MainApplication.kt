/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager

import android.app.Application
import android.os.Environment
import androidx.room.Room
import com.chibatching.kotpref.Kotpref
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.backup.NeverBackupStrategy
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator
import defpackage.taskmanager.data.local.AppDatabase
import defpackage.taskmanager.data.models.Signal
import defpackage.taskmanager.extensions.isOreoPlus
import defpackage.taskmanager.services.TasksManager
import org.jetbrains.anko.notificationManager
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidCoreModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.eagerSingleton
import org.kodein.di.generic.singleton
import java.io.File

@Suppress("unused")
class MainApplication : Application(), KodeinAware {

    override val kodein by Kodein.lazy {

        import(androidCoreModule(this@MainApplication))

        bind<TasksManager>() with singleton { TasksManager() }

        bind<AppDatabase>() with eagerSingleton {
            Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app.db")
                .build()
        }
    }

    override fun onCreate() {
        super.onCreate()
        initLogger()
        Kotpref.init(applicationContext)
        if (isOreoPlus()) {
            notificationManager.apply {
                Signal.map.forEach { (_, u) ->
                    createNotificationChannel(u.buildChannel())
                }
            }
        }
    }

    private fun initLogger() {
        val documents = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val config = LogConfiguration.Builder()
            .logLevel(LogLevel.ALL)
            .build()
        val filePrinter = FilePrinter.Builder(File(documents, "logs").path)
            .fileNameGenerator(DateFileNameGenerator())
            .backupStrategy(NeverBackupStrategy())
            .build()
        XLog.init(config, AndroidPrinter(), filePrinter)
    }
}