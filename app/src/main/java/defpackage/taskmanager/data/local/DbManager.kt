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
import defpackage.taskmanager.data.models.Event
import defpackage.taskmanager.data.models.EventTask
import defpackage.taskmanager.data.models.Task
import defpackage.taskmanager.data.models.TaskEvents
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
import java.math.BigInteger
import java.security.MessageDigest

class DbManager(context: Context) : TaskDao, EventDao {

    private var db: AppDatabase? = null

    private val dbFile = context.getDatabasePath(DB_NAME)

    val io = Mutex()

    private var md5: String? = null

    init {
        openDb(context)
    }

    val doesExist: Boolean
        get() = dbFile.exists()

    @UiThread
    private fun openDb(context: Context): Boolean {
        try {
            if (doesExist) {
                md5 = getMD5Hash(dbFile)
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
                if (exportDbInternal(preferences, false) != false) {
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
                when (exportDbInternal(preferences, true)) {
                    true -> preferences.context.toast("БД успешно сохранена")
                    null -> preferences.context.toast("Невозможно сохранить БД")
                    else -> {
                    }
                }
            }
        }
    }

    @UiThread
    private suspend fun exportDbInternal(preferences: Preferences, overwrite: Boolean): Boolean? {
        val oldPath = preferences.pathToDb
        if (!doesExist || TextUtils.isEmpty(oldPath)) {
            return null
        }
        val exportPath = if (overwrite) {
            oldPath.toString()
        } else {
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
            "${oldFile.parent ?: ""}/${oldName}_${datetime}.${hash}.${oldFile.extension}"
        }
        val exported = withContext(Dispatchers.IO) {
            copyFile(dbFile, File(exportPath))
        }
        XLog.d("Экспорт сделан $exported по пути $exportPath")
        preferences.context.apply {
            when {
                exported -> scanFile(exportPath)
                overwrite -> toast("Не удалось сохранить БД")
                else -> toast("Не удалось экспортировать БД")
            }
        }
        return exported
    }

    @WorkerThread
    suspend inline fun <T> safeExecute(action: DbManager.() -> T): T {
        return io.withLock {
            action(this)
        }
    }

    override fun insert(vararg items: Task) {
        db?.taskDao()?.insert(*items)
    }

    override fun insert(vararg items: Event) {
        db?.eventDao()?.insert(*items)
    }

    override fun update(vararg items: Task) {
        db?.taskDao()?.insert(*items)
    }

    override fun update(vararg items: Event) {
        db?.eventDao()?.update(*items)
    }

    override fun delete(vararg items: Task) {
        db?.taskDao()?.delete(*items)
    }

    override fun getAllTasks(offset: Int): List<Task> {
        return db?.taskDao()?.getAllTasks(offset).orEmpty()
    }

    override fun getActiveTasks(): List<TaskEvents> {
        return db?.taskDao()?.getActiveTasks().orEmpty()
    }

    override fun getActualEvents(offset: Int): List<EventTask> {
        return db?.eventDao()?.getActualEvents(offset).orEmpty()
    }

    override fun getEventsByTask(id: Long, offset: Int): List<Event> {
        return db?.eventDao()?.getEventsByTask(id, offset).orEmpty()
    }

    @WorkerThread
    private fun copyFile(src: File, dist: File): Boolean {
        return try {
            /*if (!src.exists()) {
                return false
            }*/
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

    @Throws(Exception::class)
    private fun getMD5Hash(file: File): String {
        FileInputStream(file).use { input ->
            return BigInteger(1, MD5.digest(input.readBytes()))
                .toString(16)
                .padStart(32, '0')
                .replace(' ', '0')
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

        private val MD5 = MessageDigest.getInstance("MD5")
    }
}
