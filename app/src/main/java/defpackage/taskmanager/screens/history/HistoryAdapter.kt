/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.history

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import defpackage.taskmanager.R
import defpackage.taskmanager.data.models.Record
import defpackage.taskmanager.screens.BaseAdapter
import defpackage.taskmanager.screens.BaseViewHolder
import org.jetbrains.anko.AnkoContext

class HistoryAdapter : BaseAdapter<Record>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(HistoryAdapterUI().createView(AnkoContext.create(parent.context, parent)))
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder<Record>(itemView) {

        private val tvText: TextView = itemView.findViewById(R.id.history_item_text)

        @SuppressLint("SetTextI18n")
        override fun onBindItem(position: Int, item: Record) {
            tvText.text = "${item.time} — ${item.description}"
        }
    }
}