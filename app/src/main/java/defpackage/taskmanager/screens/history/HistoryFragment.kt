/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import defpackage.taskmanager.EXTRA_ID
import defpackage.taskmanager.data.local.DbManager
import defpackage.taskmanager.screens.BaseFragment
import defpackage.taskmanager.widgets.EndlessScrollListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.AnkoContext
import org.kodein.di.generic.instance
import java.util.concurrent.atomic.AtomicBoolean

class HistoryFragment : BaseFragment() {

    val dbManager: DbManager by instance()

    lateinit var swipeRefresh: SwipeRefreshLayout

    lateinit var rvHistory: RecyclerView

    private val adapter = HistoryAdapter()

    private val hasEmptyQuery = AtomicBoolean(false)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        HistoryFragmentUI().createView(AnkoContext.create(activity, this))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rvHistory.addOnScrollListener(endlessListener)
        rvHistory.adapter = adapter
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
                        getEventsByTask(args.getLong(EXTRA_ID), offset)
                    }
                }
                hasEmptyQuery.set(list.isEmpty())
                items.addAll(list)
                notifyDataSetChanged()
            }
            swipeRefresh.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        rvHistory.removeOnScrollListener(endlessListener)
        super.onDestroyView()
    }

    private val endlessListener = object : EndlessScrollListener() {

        override fun onScrolledToBottom() {
            if (!hasEmptyQuery.get()) {
                loadData(adapter.items.size)
            }
        }
    }

    companion object {

        fun newInstance(id: Long): HistoryFragment {
            return HistoryFragment().apply {
                arguments = Bundle().apply {
                    putLong(EXTRA_ID, id)
                }
            }
        }
    }
}