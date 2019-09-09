/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.widgets

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import defpackage.taskmanager.R
import defpackage.taskmanager.data.models.Task
import org.jetbrains.anko.AnkoContext

class TasksAdapter(context: Context, items: ArrayList<Task>) : ArrayAdapter<Task>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder
        if (convertView == null) {
            view = TasksAdapterUI().createView(AnkoContext.create(parent.context, parent))
            viewHolder = ViewHolder(view)
        } else {
            view = convertView
            viewHolder = convertView.tag as ViewHolder
        }
        getItem(position)?.let {
            viewHolder.apply {

            }
        }
        return view
    }

    private class ViewHolder(itemView: View) {

        val tvTitle: TextView = itemView.findViewById(R.id.tasks_item_title)

        val ibComplete: ImageButton = itemView.findViewById(R.id.tasks_item_complete)

        val ibDefer: ImageButton = itemView.findViewById(R.id.tasks_item_defer)

        val ibCancel: ImageButton = itemView.findViewById(R.id.tasks_item_cancel)

        init {
            itemView.tag = this
        }
    }
}