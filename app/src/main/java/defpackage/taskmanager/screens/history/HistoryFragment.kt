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
import defpackage.taskmanager.data.models.Record
import defpackage.taskmanager.screens.BaseFragment
import org.jetbrains.anko.AnkoContext
import org.kodein.di.generic.instance

class HistoryFragment : BaseFragment() {

    val dbManager: DbManager by instance()

    lateinit var swipeRefresh: SwipeRefreshLayout

    lateinit var rvHistory: RecyclerView

    private val adapter = HistoryAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        HistoryFragmentUI().createView(AnkoContext.create(activity, this))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter.items.add(Record())
        adapter.items.add(Record())
        adapter.items.add(Record())
        adapter.items.add(Record())
        adapter.items.add(Record())
        adapter.items.add(Record())
        adapter.items.add(Record())
        adapter.items.add(Record())
        adapter.items.add(Record())
        adapter.items.add(Record())
        adapter.items.add(Record())
        adapter.items.add(Record())
        adapter.items.add(Record())
        adapter.items.add(Record())
        adapter.items.add(Record())
        adapter.items.add(Record())
        adapter.items.add(Record())
        adapter.items.add(Record())
        adapter.items.add(Record())
        adapter.items.add(Record())
        rvHistory.adapter = adapter
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