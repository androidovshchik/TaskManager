/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.tasks

import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import defpackage.taskmanager.R
import defpackage.taskmanager.data.models.Task
import defpackage.taskmanager.screens.BaseAdapter
import defpackage.taskmanager.screens.BaseViewHolder
import defpackage.taskmanager.widgets.ProgramCheckBox
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk19.listeners.onCheckedChange
import org.jetbrains.anko.sdk19.listeners.onClick

class TasksAdapter : BaseAdapter<Task>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TasksAdapterUI().createView(AnkoContext.create(parent.context, parent)))
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder<Task>(itemView) {

        val cbEnable: ProgramCheckBox = itemView.findViewById(R.id.tasks_item_box)

        val tvId: TextView = itemView.findViewById(R.id.tasks_item_id)

        val tvTitle: TextView = itemView.findViewById(R.id.tasks_item_title)

        val tvInfo: TextView = itemView.findViewById(R.id.tasks_item_info)

        val ibComplete: ImageButton = itemView.findViewById(R.id.tasks_item_complete)

        val ibDefer: ImageButton = itemView.findViewById(R.id.tasks_item_defer)

        val ibCancel: ImageButton = itemView.findViewById(R.id.tasks_item_cancel)

        val ibHistory: ImageButton = itemView.findViewById(R.id.tasks_item_history)

        val ibEdit: ImageButton = itemView.findViewById(R.id.tasks_item_edit)

        val ibDelete: ImageButton = itemView.findViewById(R.id.tasks_item_delete)

        init {
            cbEnable.onCheckedChange { _, isChecked ->
                if (!cbEnable.programmatically) {

                }
            }
            ibComplete.onClick {
                listener?.invoke(adapterPosition, items[adapterPosition], R.id.tasks_item_complete)
            }
            ibDefer.onClick {
                listener?.invoke(adapterPosition, items[adapterPosition], R.id.tasks_item_defer)
            }
            ibCancel.onClick {
                listener?.invoke(adapterPosition, items[adapterPosition], R.id.tasks_item_cancel)
            }
            ibHistory.onClick {
                listener?.invoke(adapterPosition, items[adapterPosition], R.id.tasks_item_history)
            }
        }

        override fun onBindItem(position: Int, item: Task) {
            cbEnable.setCheckedProgrammatically(item.status)
            tvId.text = item.id.toString()
            tvTitle.text = item.title
            tvInfo.text = item.title
        }
    }
}