/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.tasks

import android.view.View
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView

class TasksFragmentUI : AnkoComponent<TasksFragmentUI> {

    override fun createView(ui: AnkoContext<TasksFragmentUI>): View = with(ui) {
        recyclerView {
            lparams(width = matchParent, height = matchParent)
            owner.recyclerView =
        }
    }
}