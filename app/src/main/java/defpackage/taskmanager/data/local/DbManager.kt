/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package defpackage.taskmanager.data.local

import android.content.Context
import androidx.annotation.UiThread
import androidx.room.Room
import defpackage.taskmanager.data.models.Record
import defpackage.taskmanager.data.models.Task
import defpackage.taskmanager.extensions.isLollipopPlus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.concurrent.atomic.AtomicBoolean

class DbManager(context: Context) : TaskDao, RecordDao {

    private var db: AppDatabase? = null

    private var locked = AtomicBoolean(false)

    init {
        openDb(context)
    }

    fun openDb(context: Context) {
        Preferences.pathToDb?.let {
            closeDb()
            db = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .build()
        }
        if (Preferences.pathToDb) {

        }
    }

    @UiThread
    fun importDb(context: Context, path: String) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val file = getDatabasePath(DB_NAME)
                if (!isLollipopPlus()) {
                    val path = File("${applicationInfo.dataDir}/databases")
                    if (!path.exists()) {
                        path.mkdir()
                    }
                }
                copyFile()
            }
        }
    }

    override fun getAllTasks(): List<Task> {
        return db?.taskDao()?.getAllTasks().orEmpty()
    }

    override fun getRecordsByTask(id: Long): List<Record> {
        return db?.recordDao()?.getRecordsByTask(id).orEmpty()
    }

    @UiThread
    fun exportDb(context: Context) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                copyFile()
            }
        }
    }

    private fun copyFile(src: File, dist: File) {
        FileInputStream(src).use { input ->
            FileOutputStream(dist).use { output ->
                input.copyTo(output)
            }
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
