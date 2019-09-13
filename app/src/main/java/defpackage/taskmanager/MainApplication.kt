/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager

import android.app.Application
import android.os.Environment
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.XLog
import com.elvishew.xlog.flattener.PatternFlattener
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.backup.NeverBackupStrategy
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator
import defpackage.taskmanager.data.local.DbManager
import defpackage.taskmanager.data.models.Behavior
import defpackage.taskmanager.extensions.isOreoPlus
import net.danlew.android.joda.ResourceZoneInfoProvider
import org.jetbrains.anko.notificationManager
import org.joda.time.DateTimeZone
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinTrigger
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import java.io.File

@Suppress("unused")
class MainApplication : Application(), KodeinAware {

    override val kodein by Kodein.lazy {

        bind<DbManager>() with singleton { DbManager(applicationContext) }
    }

    override val kodeinTrigger = KodeinTrigger()

    override fun onCreate() {
        super.onCreate()
        initLogger()
        kodeinTrigger.trigger()
        DateTimeZone.setProvider(ResourceZoneInfoProvider(applicationContext))
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
            .t()
            .st(2)
            .build()
        if (BuildConfig.DEBUG) {
            getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.let {
                val filePrinter = FilePrinter.Builder(File(it, "logs").path)
                    .fileNameGenerator(DateFileNameGenerator())
                    .backupStrategy(NeverBackupStrategy())
                    .flattener(PatternFlattener("{d yyyy-MM-dd hh:mm:ss.SSS} {l}: {m}"))
                    .build()
                XLog.init(config, AndroidPrinter(), filePrinter)
                return
            }
        }
        XLog.init(config, AndroidPrinter())
    }
}