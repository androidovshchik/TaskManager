/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.tasks

import android.os.Bundle
import defpackage.taskmanager.screens.base.BaseActivity
import org.jetbrains.anko.setContentView

class TasksActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TasksActivityUI().setContentView(this)
    }

    fun onChooseDbFile() {

    }

    fun onLoadTasksFromDbFile() {

    }
}
