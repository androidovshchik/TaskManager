/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import defpackage.taskmanager.data.local.DbManager
import defpackage.taskmanager.screens.BaseFragment
import defpackage.taskmanager.services.TasksManager
import org.jetbrains.anko.AnkoContext
import org.kodein.di.generic.instance

class TasksFragment : BaseFragment() {

    val dbManager: DbManager by instance()

    val tasksManager: TasksManager by instance()

    lateinit var rvTasks: RecyclerView

    private val adapter = TasksAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle): View? =
        TasksFragmentUI().createView(AnkoContext.create(activity, this))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rvTasks.adapter = adapter
    }
}