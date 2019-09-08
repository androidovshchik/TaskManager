/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package defpackage.taskmanager.data.local

import android.content.Context
import android.text.TextUtils
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

    /**
     * Indicates I/O operations during import/export
     */
    val io = MutableLiveData(false)

    private var db: AppDatabase? = null

    private val dbFile = context.getDatabasePath(DB_NAME)

    init {
        openDb(context)
    }

    val isOpen: Boolean
        get() = db?.isOpen == true

    private fun openDb(context: Context) {
        closeDb()
        if (dbFile.exists()) {
            db = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .build()
        }
    }

    @UiThread
    fun importDb(context: Context, path: String) {
        if (io.value == true) {
            XLog.w("Импорт БД во время I/O операций")
            context.toast("Подождите...")
            return
        }
        io.value = true
        GlobalScope.launch(Dispatchers.Main) {
            closeDb()
            val copied = withContext(Dispatchers.IO) {
                copyFile(File(path), dbFile)
            }
            context.run {
                if (copied) {
                    openDb(context)
                    toast("БД успешно импортирована")
                } else {
                    toast("Не удалось импортировать БД")
                }
            }
            io.value = false
        }
    }

    override fun getAllTasks(): List<Task> {
        if (isOpen) {
            if (io.value == false) {
                return db?.taskDao()?.getAllTasks().orEmpty()
            } else {
                XLog.w("Работа с БД во время I/O операций")
            }
        }
        return arrayListOf()
    }

    override fun getRecordsByTask(id: Long): List<Record> {
        if (isOpen) {
            if (io.value == false) {
                return db?.recordDao()?.getRecordsByTask(id).orEmpty()
            } else {
                XLog.w("Работа с БД во время I/O операций")
            }
        }
        return arrayListOf()
    }

    @UiThread
    fun exportDb(context: Context, path: String?) {
        if (TextUtils.isEmpty(path?.trim())) {
            XLog.w("Пустой путь для экспорта БД")
            context.toast("Не выбрана БД")
            return
        }
        if (!dbFile.exists()) {
            val message = "Не существует файла с БД"
            XLog.w(message)
            context.toast(message)
            return
        }
        if (io.value == true) {
            XLog.w("Экспорт БД во время I/O операций")
            context.toast("Подождите...")
            return
        }
        io.value = true
        GlobalScope.launch(Dispatchers.Main) {
            val copied = withContext(Dispatchers.IO) {
                copyFile(dbFile, File(path))
            }
            context.run {
                if (copied) {
                    scanFile(path!!)
                    toast("БД успешно экспортирована")
                } else {
                    toast("Не удалось экспортировать БД")
                }
            }
            io.value = false
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
