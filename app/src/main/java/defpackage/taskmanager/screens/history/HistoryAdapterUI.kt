/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.history

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import defpackage.taskmanager.R
import org.jetbrains.anko.*

class HistoryAdapterUI : AnkoComponent<ViewGroup> {

    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        linearLayout {
            lparams(matchParent)
            gravity = Gravity.CENTER_VERTICAL
            textView {
                id = R.id.tasks_item_id
                textSize = 15f
                textColor = Color.BLACK
            }.lparams()
            textView {
                id = R.id.tasks_item_title
                textSize = 15f
                textColor = Color.BLACK
            }.lparams(0) {
                weight = 1f
            }
        }
    }
}