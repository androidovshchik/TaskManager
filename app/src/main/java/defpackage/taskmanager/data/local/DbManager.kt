/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package defpackage.taskmanager.data.local

import android.content.Context
import android.text.TextUtils
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.room.Room
import com.elvishew.xlog.XLog
import defpackage.taskmanager.PATTERN_DATETIME
import defpackage.taskmanager.data.models.Record
import defpackage.taskmanager.data.models.TaskCount
import defpackage.taskmanager.extensions.scanFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.jetbrains.anko.toast
import org.joda.time.LocalDateTime
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class DbManager(context: Context) : TaskDao, RecordDao {

    private var db: AppDatabase? = null

    private val dbFile = context.getDatabasePath(DB_NAME)

    private val io = Mutex()

    init {
        openDb(context)
    }

    val doesExist: Boolean
        get() = dbFile.exists()

    @UiThread
    private fun openDb(context: Context): Boolean {
        try {
            if (doesExist) {
                db = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                    .build()
                return true
            }
        } catch (e: Exception) {
            XLog.e(e.localizedMessage, e)
            context.toast("Не удалось инициализировать БД")
        }
        return false
    }

    @UiThread
    fun importDb(preferences: Preferences, newPath: String) {
        if (!SQLITE_REGEX.matches(newPath)) {
            if (!TextUtils.isEmpty(newPath)) {
                XLog.w("Невалидный путь: $newPath")
            }
            preferences.context.toast("Недопустимый файл для импорта БД")
            return
        }
        GlobalScope.launch(Dispatchers.Main) {
            io.withLock {
                closeDb()
                if (exportDbInternal(preferences) != false) {
                    if (importDbInternal(preferences, newPath)) {
                        preferences.context.toast("БД успешно импортирована")
                    }
                }
            }
        }
    }

    @UiThread
    private suspend fun importDbInternal(preferences: Preferences, newPath: String): Boolean {
        val imported = withContext(Dispatchers.IO) {
            copyFile(File(newPath), dbFile)
        }
        XLog.d("Импорт сделан $imported по пути $newPath")
        preferences.apply {
            if (imported && openDb(context)) {
                pathToDb = newPath
            } else {
                context.toast("Не удалось импортировать БД")
            }
        }
        return imported
    }

    @UiThread
    fun exportDb(preferences: Preferences) {
        GlobalScope.launch(Dispatchers.Main) {
            io.withLock {
                when (exportDbInternal(preferences)) {
                    true -> preferences.context.toast("БД успешно экспортирована")
                    null -> preferences.context.toast("Невозможно экспортировать БД")
                    else -> {
                    }
                }
            }
        }
    }

    @UiThread
    private suspend fun exportDbInternal(preferences: Preferences): Boolean? {
        val oldPath = preferences.pathToDb
        if (!doesExist || TextUtils.isEmpty(oldPath)) {
            return null
        }
        val oldFile = File(oldPath)
        val oldName = oldFile.nameWithoutExtension
            .split("_")[0]
        val now = LocalDateTime.now()
        val datetime = now.toString(PATTERN_DATETIME)
            .replace(" ", ".")
            .replace(":", ".")
        val hash = now.millisOfSecond().get()
            .toString()
            .padStart(3, '0')
        val exportPath = "${oldFile.parent ?: ""}/${oldName}_${datetime}.${hash}.${oldFile.extension}"
        val exported = withContext(Dispatchers.IO) {
            copyFile(dbFile, File(exportPath))
        }
        XLog.d("Экспорт сделан $exported по пути $exportPath")
        preferences.context.apply {
            if (exported) {
                scanFile(exportPath)
            } else {
                toast("Не удалось экспортировать БД")
            }
        }
        return exported
    }

    @WorkerThread
    suspend fun getAllTasks(): List<TaskCount> {
        return io.withLock {
            getAllTasksInternal()
        }
    }

    override fun getAllTasksInternal(): List<TaskCount> {
        return db?.taskDao()?.getAllTasksInternal().orEmpty()
    }

    @WorkerThread
    suspend fun getRecordsByTask(id: Long): List<Record> {
        return io.withLock {
            getRecordsByTaskInternal(id)
        }
    }

    override fun getRecordsByTaskInternal(id: Long): List<Record> {
        return db?.recordDao()?.getRecordsByTaskInternal(id).orEmpty()
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

    @UiThread
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
