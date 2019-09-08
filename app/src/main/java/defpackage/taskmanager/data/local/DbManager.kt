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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    val doesExist: Boolean
        get() = dbFile.exists()

    val isOpened: Boolean
        get() = db?.isOpen == true

    private fun openDb(context: Context) {
        closeDb()
        if (doesExist) {
            db = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .build()
        }
    }

    @UiThread
    fun importDb(preferences: Preferences, path: String?) = path?.let {
        if (SQLITE_REGEX.matches(path.toString())) {

        }
        preferences.pathToDb = etDbPath.text.toString()
        if (TextUtils.isEmpty(path.trim())) {
            XLog.w("Не задан путь для импорта БД")
            context.toast("Не выбрана БД")
            return
        }
        if (SQLITE_REGEX.matches(path)) {
            preferences.pathToDb = path
            etDbPath.setText(path)
        } else {
            toast("Выберите файл со sqlite БД")
        }
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
            if (!dbFile.exists()) {
                val message = "Не существует файла с БД"
                XLog.w(message)
                return
            }
            val copied = withContext(Dispatchers.IO) {
                copyFile(dbFile, File(path))
            }
            context.run {
                if (copied) {
                    openDb(context)
                    toast("БД успешно импортирована")
                } else {
                    toast("Не удалось импортировать БД")
                }
                scanFile(path)
            }
            io.value = false
        }
    }

    override fun getAllTasks(): List<Task> {
        if (isOpened) {
            if (io.value == false) {
                return db?.taskDao()?.getAllTasks().orEmpty()
            } else {
                XLog.w("Работа с БД во время I/O операций")
            }
        }
        return arrayListOf()
    }

    override fun getRecordsByTask(id: Long): List<Record> {
        if (isOpened) {
            if (io.value == false) {
                return db?.recordDao()?.getRecordsByTask(id).orEmpty()
            } else {
                XLog.w("Работа с БД во время I/O операций")
            }
        }
        return arrayListOf()
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

        @JvmStatic
        private val SQLITE_REGEX = ".+\\.(db|sdb|sqlite|db3|s3db|sqlite3|sl3)$".toRegex()
    }
}
