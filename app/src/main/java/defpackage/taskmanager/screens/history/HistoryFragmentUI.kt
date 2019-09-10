/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.history

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import defpackage.taskmanager.R
import defpackage.taskmanager.extensions.swipeRefreshLayout
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView

class HistoryFragmentUI : AnkoComponent<HistoryFragment> {

    override fun createView(ui: AnkoContext<HistoryFragment>): View = with(ui) {
        owner.swipeRefresh = swipeRefreshLayout {
            layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
            owner.rvHistory = recyclerView {
                layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL).apply {
                    ContextCompat.getDrawable(context, R.drawable.divider)?.let {
                        setDrawable(it)
                    }
                })
            }
        }
        owner.swipeRefresh
    }
}