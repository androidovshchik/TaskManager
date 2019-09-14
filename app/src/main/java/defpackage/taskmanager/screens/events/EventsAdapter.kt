/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.events

import android.annotation.SuppressLint
import android.graphics.Paint
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

class EventsAdapter : BaseAdapter<Task>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(EventsAdapterUI().createView(AnkoContext.create(parent.context, parent)))
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder<Task>(itemView) {

        private val cbEnable: ProgramCheckBox = itemView.findViewById(R.id.tasks_item_box)

        private val tvId: TextView = itemView.findViewById(R.id.tasks_item_id)

        private val tvTitle: TextView = itemView.findViewById(R.id.tasks_item_title)

        private val tvInfo: TextView = itemView.findViewById(R.id.tasks_item_info)

        private val ibComplete: ImageButton = itemView.findViewById(R.id.tasks_item_complete)

        private val ibDefer: ImageButton = itemView.findViewById(R.id.tasks_item_defer)

        private val ibCancel: ImageButton = itemView.findViewById(R.id.tasks_item_cancel)

        private val ibHistory: ImageButton = itemView.findViewById(R.id.tasks_item_history)

        private val ibEdit: ImageButton = itemView.findViewById(R.id.tasks_item_edit)

        private val ibDelete: ImageButton = itemView.findViewById(R.id.tasks_item_delete)

        init {
            cbEnable.onCheckedChange { _, isChecked ->
                if (!cbEnable.programmatically) {
                    items[adapterPosition].also {
                        it.status = isChecked
                        listener?.invoke(adapterPosition, it, R.id.tasks_item_box)
                    }
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

        @SuppressLint("SetTextI18n")
        override fun onBindItem(position: Int, item: Task) {
            cbEnable.setCheckedProgrammatically(item.status)
            tvId.text = item.id.toString()
            tvTitle.text = item.title
            tvInfo.text = "Время: ${item.getPossibleTime()}"
            if (item.isValid) {
                tvTitle.paintFlags = tvTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                tvInfo.paintFlags = tvInfo.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            } else {
                tvTitle.paintFlags = tvTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                tvInfo.paintFlags = tvInfo.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        }
    }
}