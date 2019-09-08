/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package defpackage.taskmanager.data.local

import android.content.Context
import androidx.room.Room
import defpackage.taskmanager.extensions.isLollipopPlus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.concurrent.atomic.AtomicBoolean

class DbManager {

    var db: AppDatabase? = null

    var locked = AtomicBoolean(false)

    suspend fun openDb(context: Context, path: String) {
        closeDb()
        withContext(Dispatchers.IO) {

        }
        db = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
            .build()
    }

    fun importDb(path: String) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {

            }
        }
    }

    fun exportDb(path: String) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {

            }
        }
    }

    private fun copyDbFile(context: Context) = context.run {
        val file = getDatabasePath(DB_NAME)
        if (!isLollipopPlus()) {
            val path = File("${applicationInfo.dataDir}/databases")
            if (!path.exists()) {
                path.mkdir()
            }
        }
        if (!file.exists()) {
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
