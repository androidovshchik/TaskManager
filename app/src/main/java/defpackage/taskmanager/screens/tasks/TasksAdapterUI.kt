/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.tasks

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import defpackage.taskmanager.R
import defpackage.taskmanager.extensions.setImageXmlDrawable
import defpackage.taskmanager.widgets.programCheckBox
import org.jetbrains.anko.*

class TasksAdapterUI : AnkoComponent<ViewGroup> {

    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        verticalLayout {
            lparams(matchParent)
            padding = dip(8)
            linearLayout {
                lparams(matchParent)
                gravity = Gravity.CENTER_VERTICAL
                programCheckBox {
                    id = R.id.tasks_item_box
                }.lparams()
                textView {
                    id = R.id.tasks_item_id
                    textSize = 16f
                    textColor = Color.BLACK
                }.lparams()
                textView {
                    id = R.id.tasks_item_title
                    textSize = 15f
                    textColor = Color.BLACK
                }.lparams(0) {
                    weight = 1f
                    marginStart = dip(8)
                }
                imageButton {
                    id = R.id.tasks_item_edit
                    isEnabled = false
                    setImageXmlDrawable(R.drawable.ic_edit_black_24dp)
                }.lparams()
                imageButton {
                    id = R.id.tasks_item_delete
                    isEnabled = false
                    setImageXmlDrawable(R.drawable.ic_delete_black_24dp)
                }.lparams()
            }
            linearLayout {
                lparams(matchParent)
                gravity = Gravity.CENTER_VERTICAL
                textView {
                    id = R.id.tasks_item_info
                    textSize = 15f
                    textColor = Color.BLACK
                }.lparams(0) {
                    weight = 1f
                }
                imageButton {
                    id = R.id.tasks_item_complete
                    setImageResource(R.drawable.ic_done_black_24dp)
                }.lparams()
                imageButton {
                    id = R.id.tasks_item_defer
                    setImageResource(R.drawable.ic_update_black_24dp)
                }.lparams()
                imageButton {
                    id = R.id.tasks_item_cancel
                    setImageResource(R.drawable.ic_close_black_24dp)
                }.lparams()
                imageButton {
                    id = R.id.tasks_item_history
                    setImageXmlDrawable(R.drawable.ic_info_black_24dp)
                }.lparams()
            }
        }
    }
}