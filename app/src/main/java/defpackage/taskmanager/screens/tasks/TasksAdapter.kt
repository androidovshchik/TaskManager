/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.tasks

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.AnkoContext

class TasksAdapter : RecyclerView.Adapter<TasksAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(TasksAdapterUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = list[position]
        holder.tvTitle.text = movie.title
        holder.tvYear.text = "${movie.year}"
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvTitle: TextView
        var tvYear: TextView

        init {
            tvTitle = itemView.findViewById(TasksAdapterUI.tvTitleId)
            tvYear = itemView.findViewById(TasksAdapterUI.tvYearId)
        }

    }
}