/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import defpackage.taskmanager.*
import defpackage.taskmanager.data.local.DbManager
import defpackage.taskmanager.receivers.ActionReceiver
import defpackage.taskmanager.screens.BaseFragment
import defpackage.taskmanager.screens.history.HistoryActivity
import defpackage.taskmanager.widgets.EndlessScrollListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.intentFor
import org.kodein.di.generic.instance

class EventsFragment : BaseFragment() {

    val dbManager: DbManager by instance()

    lateinit var swipeRefresh: SwipeRefreshLayout

    lateinit var rvEvents: RecyclerView

    private val adapter = EventsAdapter()

    private var hasEmptyQuery = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        EventsFragmentUI().createView(AnkoContext.create(activity, this))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter.setAdapterListener { _, item, param ->
            appContext?.let {
                when (param) {
                    R.id.tasks_item_complete -> {
                        it.sendBroadcast(
                            it.intentFor<ActionReceiver>(
                                EXTRA_TASK to item.id,
                                EXTRA_ACTION to ACTION_COMPLETED
                            )
                        )
                    }
                    R.id.tasks_item_defer -> {
                        it.sendBroadcast(
                            it.intentFor<ActionReceiver>(
                                EXTRA_TASK to item.id,
                                EXTRA_ACTION to ACTION_DEFERRED
                            )
                        )
                    }
                    R.id.tasks_item_cancel -> {
                        it.sendBroadcast(
                            it.intentFor<ActionReceiver>(
                                EXTRA_TASK to item.id,
                                EXTRA_ACTION to ACTION_CANCELLED
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
        rvEvents.adapter = adapter
        loadData(0)
    }

    fun loadData(offset: Int) {
        fragmentJob.cancelChildren()
        if (offset <= 0) {
            adapter.items.clear()
        }
        launch {
            adapter.apply {
                val list = withContext(Dispatchers.IO) {
                    dbManager.safeExecute {
                        getActualEvents(offset)
                    }
                }
                hasEmptyQuery = list.isEmpty()
                items.addAll(list)
                notifyDataSetChanged()
            }
            swipeRefresh.isRefreshing = false
        }
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

    override fun clearData() {
        fragmentJob.cancelChildren()
        adapter.apply {
            items.clear()
            notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        rvEvents.removeOnScrollListener(endlessListener)
        super.onDestroyView()
    }

    private val endlessListener = object : EndlessScrollListener() {

        override fun onScrolledToBottom() {
            if (!hasEmptyQuery) {
                loadData(adapter.items.size)
            }
        }
    }
}