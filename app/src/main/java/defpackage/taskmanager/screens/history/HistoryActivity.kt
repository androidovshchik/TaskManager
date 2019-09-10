/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.history

import android.os.Bundle
import defpackage.taskmanager.screens.BaseActivity
import org.jetbrains.anko.setContentView

class HistoryActivity : BaseActivity() {

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HistoryActivityUI().setContentView(this)
        fragmentManager.beginTransaction()
            .add(TasksActivityUI.FRAME_LAYOUT_ID, tasksFragment)
            .commit()
    }
}
