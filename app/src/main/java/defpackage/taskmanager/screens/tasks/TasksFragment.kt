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
import defpackage.taskmanager.EXTRA_STATUS
import defpackage.taskmanager.EXTRA_TASK
import defpackage.taskmanager.R
import defpackage.taskmanager.data.local.DbManager
import defpackage.taskmanager.data.models.Record
import defpackage.taskmanager.receivers.ActionReceiver
import defpackage.taskmanager.screens.BaseFragment
import defpackage.taskmanager.screens.history.HistoryActivity
import defpackage.taskmanager.services.TasksService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.intentFor
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
            appContext?.let {
                when (param) {
                    R.id.tasks_item_box -> {
                        preferences?.run {
                            TasksService.launch(this, EXTRA_TASK to item.id)
                        }
                    }
                    R.id.tasks_item_complete -> {
                        it.sendBroadcast(
                            it.intentFor<ActionReceiver>(
                                EXTRA_TASK to item.id,
                                EXTRA_STATUS to Record.STATUS_COMPLETED
                            )
                        )
                    }
                    R.id.tasks_item_defer -> {
                        it.sendBroadcast(
                            it.intentFor<ActionReceiver>(
                                EXTRA_TASK to item.id,
                                EXTRA_STATUS to Record.STATUS_DEFERRED
                            )
                        )
                    }
                    R.id.tasks_item_cancel -> {
                        it.sendBroadcast(
                            it.intentFor<ActionReceiver>(
                                EXTRA_TASK to item.id,
                                EXTRA_STATUS to Record.STATUS_CANCELLED
                            )
                        )
                    }
                    R.id.tasks_item_history -> {
                        HistoryActivity.launch(it, item.id, item.title)
                    }
                    else -> {
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

    fun clearData() {
        fragmentJob.cancelChildren()
        adapter.apply {
            items.clear()
            notifyDataSetChanged()
        }
    }
}