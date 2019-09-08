/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package defpackage.taskmanager.data.local

import android.content.Context
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
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

class DbManager(context: Context) : TaskDao, RecordDao {

    private var db: AppDatabase? = null

    /**
     * Indicates I/O operations during import/export
     */
    val io = MutableLiveData(true)

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
        io.value = false
    }

    @UiThread
    fun importDb(context: Context, path: String) {
        io.value = true
        GlobalScope.launch(Dispatchers.Main) {
            val copied = withContext(Dispatchers.IO) {
                copyFile(File(path), dbFile)
            }
            if (copied) {
                openDb(context)
            } else {
                closeDb()
                io.value = false
            }
        }
    }

    override fun getAllTasks(): List<Task> {
        if (io.value == false) {
            return db?.taskDao()?.getAllTasks().orEmpty()
        }
        return arrayListOf()
    }

    override fun getRecordsByTask(id: Long): List<Record> {
        if (io.value == false) {
            return db?.recordDao()?.getRecordsByTask(id).orEmpty()
        }
        return arrayListOf()
    }

    @UiThread
    fun exportDb(context: Context, path: String?) {
        path?.let {
            io.value = true
            GlobalScope.launch(Dispatchers.Main) {
                val copied = withContext(Dispatchers.IO) {
                    copyFile(dbFile, File(it))
                }
                context.run {
                    if (copied) {
                        scanFile(it)
                        toast("БД экспортирована")
                    } else {
                        toast("Не удалось экспортировать")
                    }
                }
                io.value = false
            }
        } ?: run {
            context.toast("Не выбрана БД")
        }
    }

    @WorkerThread
    private fun copyFile(src: File, dist: File): Boolean {
        return try {
            dist.parentFile.apply {
                if (!exists()) {
                    mkdir()
                }
            }
            FileInputStream(src).use { input ->
                FileOutputStream(dist).use { output ->
                    input.copyTo(output)
                }
            }
            true
        } catch (e: Exception) {
            XLog.e(e.localizedMessage, e)
            dist.delete()
            false
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
