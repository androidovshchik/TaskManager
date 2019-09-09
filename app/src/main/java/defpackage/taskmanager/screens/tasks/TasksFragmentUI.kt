/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.tasks

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import defpackage.taskmanager.R
import defpackage.taskmanager.extensions.nestedScrollView
import defpackage.taskmanager.extensions.swipeRefreshLayout
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

class TasksFragmentUI : AnkoComponent<TasksFragment> {

    override fun createView(ui: AnkoContext<TasksFragment>): View = with(ui) {
        owner.swipeRefresh = swipeRefreshLayout {
            layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
            nestedScrollView {
                layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
                verticalLayout {
                    lparams(matchParent, matchParent)
                    owner.tvTasks = textView("Список задач") {
                        padding = dip(8)
                        textSize = 15f
                        textColor = Color.BLACK
                    }.lparams(matchParent)
                    view {
                        background = ContextCompat.getDrawable(context, R.drawable.divider)
                    }.lparams(matchParent, dip(1))
                    owner.rvTasks = recyclerView {
                        layoutManager = LinearLayoutManager(context)
                        isNestedScrollingEnabled = false
                        addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL).apply {
                            ContextCompat.getDrawable(context, R.drawable.divider)?.let {
                                setDrawable(it)
                            }
                        })
                    }.lparams(matchParent, 0) {
                        weight = 1f
                    }
                }
            }
        }
        owner.swipeRefresh
    }
}