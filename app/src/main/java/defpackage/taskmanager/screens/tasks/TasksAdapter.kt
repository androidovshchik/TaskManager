/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.tasks

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import defpackage.taskmanager.data.models.Task
import org.jetbrains.anko.AnkoContext

class TasksAdapter : RecyclerView.Adapter<TasksAdapter.MovieViewHolder>() {

    val items = arrayListOf<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(TasksAdapterUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = items[position]

    }

    override fun getItemCount() = items.size

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvId: TextView = itemView.findViewById(TasksAdapterUI.tvTitleId)
    }
}