/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import defpackage.taskmanager.R
import defpackage.taskmanager.data.local.DbManager
import defpackage.taskmanager.screens.BaseFragment
import defpackage.taskmanager.screens.history.HistoryActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.AnkoContext
import org.kodein.di.generic.instance

class TasksFragment : BaseFragment() {

    val dbManager: DbManager by instance()

    lateinit var tvTasks: TextView

    lateinit var swipeRefresh: SwipeRefreshLayout

    lateinit var rvTasks: RecyclerView

    private val adapter = TasksAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        TasksFragmentUI().createView(AnkoContext.create(activity, this))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter.setAdapterListener { _, item, param ->
            when (param) {
                R.id.tasks_item_history -> {
                    appContext?.let {
                        HistoryActivity.launch(it, item.id, item.title)
                    }
                }
            }
        }
        rvTasks.adapter = adapter
        onRefreshData()
    }

    fun onRefreshData() {
        fragmentJob.cancelChildren()
        launch {
            adapter.apply {
                items.clear()
                items.addAll(withContext(Dispatchers.IO) {
                    dbManager.getAllTasks()
                })
                notifyDataSetChanged()
                swipeRefresh.isRefreshing = false
            }
        }
    }
}