/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.history

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import defpackage.taskmanager.R
import org.jetbrains.anko.*

class HistoryAdapterUI : AnkoComponent<ViewGroup> {

    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        frameLayout {
            lparams(matchParent)
            padding = dip(8)
            textView {
                id = R.id.history_item_text
                textSize = 15f
                textColor = Color.BLACK
            }.lparams(matchParent)
        }
    }
}