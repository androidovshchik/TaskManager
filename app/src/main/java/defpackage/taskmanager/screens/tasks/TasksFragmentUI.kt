/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.tasks

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import defpackage.taskmanager.extensions.swipeRefreshLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

class TasksFragmentUI : AnkoComponent<TasksFragment> {

    override fun createView(ui: AnkoContext<TasksFragment>): View = with(ui) {
        verticalLayout {
            lparams(matchParent, matchParent)
            owner.tvTasks = textView {
                textSize = 15f
                textColor = Color.BLACK
            }.lparams(matchParent)
            owner.swipeRefresh = swipeRefreshLayout {
                owner.rvTasks = recyclerView {
                    layoutManager = LinearLayoutManager(context)
                }.lparams(matchParent, matchParent)
            }.lparams(matchParent, 0) {
                weight = 1f
            }
        }
    }
}