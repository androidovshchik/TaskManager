/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.widgets

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import defpackage.taskmanager.R
import defpackage.taskmanager.extensions.setImageXmlDrawable
import org.jetbrains.anko.*

class TasksAdapterUI : AnkoComponent<ViewGroup> {

    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        linearLayout {
            lparams(matchParent)
            padding = dip(8)
            gravity = Gravity.CENTER_VERTICAL
            textView {
                id = R.id.tasks_item_title
                textSize = 15f
                textColor = Color.BLACK
            }.lparams(0) {
                weight = 1f
            }
            imageButton {
                id = R.id.tasks_item_complete
                setImageXmlDrawable(R.drawable.ic_done_black_24dp)
            }.lparams()
            imageButton {
                id = R.id.tasks_item_defer
                setImageXmlDrawable(R.drawable.ic_update_black_24dp)
            }.lparams()
            imageButton {
                id = R.id.tasks_item_cancel
                setImageXmlDrawable(R.drawable.ic_close_black_24dp)
            }.lparams()
        }
    }
}