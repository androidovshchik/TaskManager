/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.tasks

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView

class TasksFragmentUI : AnkoComponent<TasksFragment> {

    override fun createView(ui: AnkoContext<TasksFragment>): View = with(ui) {
        owner.recyclerView = recyclerView {
            lparams(width = matchParent, height = matchParent)
            layoutManager = LinearLayoutManager(context)
        }
        owner.recyclerView
    }
}