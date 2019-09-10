/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import defpackage.taskmanager.data.local.DbManager
import defpackage.taskmanager.data.models.Task
import defpackage.taskmanager.screens.BaseFragment
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
        dbManager.io.observeForever(dbObserver)
        adapter.items.add(Task())
        adapter.items.add(Task())
        adapter.items.add(Task())
        adapter.items.add(Task())
        adapter.items.add(Task())
        adapter.items.add(Task())
        adapter.items.add(Task())
        adapter.items.add(Task())
        adapter.items.add(Task())
        adapter.items.add(Task())
        adapter.items.add(Task())
        adapter.items.add(Task())
        adapter.items.add(Task())
        adapter.items.add(Task())
        adapter.items.add(Task())
        adapter.items.add(Task())
        adapter.items.add(Task())
        adapter.items.add(Task())
        adapter.items.add(Task())
        adapter.items.add(Task())
        adapter.items.add(Task())
        adapter.items.add(Task())
        rvTasks.adapter = adapter
    }

    override fun onDestroyView() {
        dbManager.io.removeObserver(dbObserver)
        super.onDestroyView()
    }

    private val dbObserver = Observer<Boolean> {

    }
}