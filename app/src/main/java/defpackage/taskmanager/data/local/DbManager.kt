/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package defpackage.taskmanager.data.local

import android.content.Context
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.room.Room
import com.elvishew.xlog.XLog
import defpackage.taskmanager.data.models.Record
import defpackage.taskmanager.data.models.Task
import defpackage.taskmanager.extensions.scanFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.concurrent.atomic.AtomicBoolean

class DbManager(context: Context) : TaskDao, RecordDao {

    private var db: AppDatabase? = null

    /**
     * Indicates I/O operations during import/export
     */
    private var io = AtomicBoolean(false)

    private val dbFile = context.getDatabasePath(DB_NAME)

    init {
        openDb(context)
    }

    fun openDb(context: Context) {
        if (dbFile.exists()) {
            closeDb()
            db = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .build()
        }
    }

    @UiThread
    fun importDb(context: Context, path: String) {
        io.set(true)
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                copyFile(File(path), dbFile)
            }
            io.set(false)
        }
    }

    override fun getAllTasks(): List<Task> {
        return db?.taskDao()?.getAllTasks().orEmpty()
    }

    override fun getRecordsByTask(id: Long): List<Record> {
        return db?.recordDao()?.getRecordsByTask(id).orEmpty()
    }

    @UiThread
    fun exportDb(context: Context, path: String?) {
        path?.let {
            io.set(true)
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    copyFile(dbFile, File(it))
                }
                context.run {
                    scanFile(it)
                    toast("БД экспортирована")
                }
                io.set(false)
            }
        } ?: context.toast("Не выбрана БД")
    }

    @WorkerThread
    private fun copyFile(src: File, dist: File) {
        dist.parentFile.apply {
            if (!exists()) {
                mkdir()
            }
        }
        try {
            FileInputStream(src).use { input ->
                FileOutputStream(dist).use { output ->
                    input.copyTo(output)
                }
            }
        } catch (e: Exception) {
            XLog.e(e.localizedMessage, e)
            dist.delete()
        }
    }

    fun closeDb() {
        db?.apply {
            close()
            db = null
        }
    }

    companion object {

        private const val DB_NAME = "app.db"
    }
}
