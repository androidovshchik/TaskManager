/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.widgets

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import defpackage.taskmanager.R
import defpackage.taskmanager.data.models.Task
import org.jetbrains.anko.AnkoContext

class TasksAdapter(context: Context, items: ArrayList<Task>) : ArrayAdapter<Task>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val user = getItem(position)
        val viewHolder: ViewHolder
        if (convertView == null) {
            convertView = TasksAdapterUI().createView(AnkoContext.create(parent.context, parent))
            viewHolder = ViewHolder(convertView)
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        viewHolder.name!!.setText(user!!.name)
        viewHolder.home!!.setText(user.hometown)
        return convertView
    }

    private class ViewHolder(itemView: View) {

        val checkBox: ImageView = itemView.findViewById(R.id.tasks_item_box)

        val tvId: TextView = itemView.findViewById(R.id.tasks_item_id)

        val tvTitle: TextView = itemView.findViewById(R.id.tasks_item_title)

        val tvInfo: TextView = itemView.findViewById(R.id.tasks_item_info)

        val ibComplete: ImageButton = itemView.findViewById(R.id.tasks_item_complete)

        val ibDefer: ImageButton = itemView.findViewById(R.id.tasks_item_defer)

        val ibCancel: ImageButton = itemView.findViewById(R.id.tasks_item_cancel)

        val ibHistory: ImageButton = itemView.findViewById(R.id.tasks_item_history)

        val ibEdit: ImageButton = itemView.findViewById(R.id.tasks_item_edit)

        val ibDelete: ImageButton = itemView.findViewById(R.id.tasks_item_delete)
    }
}