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
import defpackage.taskmanager.PATTERN_DATETIME
import defpackage.taskmanager.data.models.Record
import defpackage.taskmanager.data.models.TaskCount
import defpackage.taskmanager.extensions.scanFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.toast
import org.joda.time.LocalDateTime
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
        if (doesExist) {
            db = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .build()
            XLog.d(isOpened)
        }
    }

    @UiThread
    fun importDb(preferences: Preferences, newPath: String) {
        if (!SQLITE_REGEX.matches(newPath)) {
            XLog.w("Невалидный путь: $newPath")
            preferences.context.toast("Невалидный файл для импорта БД")
            return
        }
        if (io.value == true) {
            XLog.w("Импорт БД во время I/O операций")
            preferences.context.toast("Подождите...")
            return
        }
        io.value = true
        GlobalScope.launch(Dispatchers.Main) {
            closeDb()
            if (exportInternal(preferences)) {
                val imported = withContext(Dispatchers.IO) {
                    copyFile(File(newPath), dbFile)
                }
                XLog.d("Импорт сделан $imported по пути $newPath")
                if (imported) {
                    preferences.apply {
                        openDb(context)
                        pathToDb = newPath
                        context.toast("БД успешно импортирована")
                    }
                } else {
                    preferences.context.toast("Не удалось импортировать БД")
                }
            } else {
                preferences.context.toast("Не удалось экспортировать БД")
            }
            io.value = false
        }
    }

    @UiThread
    fun exportDb(preferences: Preferences) {
        if (io.value == true) {
            XLog.w("Экспорт БД во время I/O операций")
            preferences.context.toast("Подождите...")
            return
        }
        io.value = true
        GlobalScope.launch(Dispatchers.Main) {
            preferences.context.apply {
                if (exportInternal(preferences)) {
                    toast("БД успешно экспортирована")
                } else {
                    toast("Не удалось экспортировать БД")
                }
            }
            io.value = false
        }
    }

    private suspend fun exportInternal(preferences: Preferences): Boolean {
        val oldPath = preferences.pathToDb
        if (!doesExist || TextUtils.isEmpty(oldPath)) {
            return false
        }
        val oldFile = File(oldPath)
        val oldName = oldFile.nameWithoutExtension
            .split("_")[0]
        val datetime = LocalDateTime.now()
            .toString(PATTERN_DATETIME)
            .replace(" ", "_")
        val hash = (0..999).random()
            .toString()
            .padStart(3, '0')
        val exportPath = "${oldFile.parent ?: ""}/${oldName}_${datetime}_${hash}.${oldFile.extension}"
        val exported = withContext(Dispatchers.IO) {
            copyFile(dbFile, File(exportPath))
        }
        XLog.d("Экспорт сделан $exported по пути $exportPath")
        if (exported) {
            preferences.context.scanFile(exportPath)
        }
        return exported
    }

    override fun getAllTasks(): List<TaskCount> {
        //if (isOpened) {
            if (io.value == false) {
                return db?.taskDao()?.getAllTasks().orEmpty()
            } else {
                XLog.w("Работа с БД во время I/O операций")
            }
        //}
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

        private val SQLITE_REGEX = ".+\\.(db|sdb|sqlite|db3|s3db|sqlite3|sl3)$".toRegex()
    }
}
