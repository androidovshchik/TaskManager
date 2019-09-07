/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.tasks

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import defpackage.taskmanager.R
import org.jetbrains.anko.*

class TasksAdapterUI : AnkoComponent<ViewGroup> {

    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        verticalLayout {
            lparams(matchParent)
            padding = dip(8)
            linearLayout {
                lparams(matchParent, wrapContent)
                gravity = Gravity.CENTER_VERTICAL
                textView {
                    id = R.id.tasks_item_id
                    textSize = 15f
                    textColor = Color.BLACK
                }.lparams()
                textView {
                    id = R.id.tasks_item_id
                    textSize = 15f
                    textColor = Color.BLACK
                }.lparams()
                imageButton {
                    id = R.id.tasks_item_id
                }.lparams()
                imageButton {
                    id = R.id.tasks_item_id
                }.lparams()
            }
            linearLayout {
                lparams(matchParent, wrapContent)
                gravity = Gravity.CENTER_VERTICAL
                textView {
                    id = R.id.tasks_item_id
                    textSize = 15f
                    textColor = Color.BLACK
                }.lparams()
                imageButton {
                    id = R.id.tasks_item_id
                }.lparams()
                imageButton {
                    id = R.id.tasks_item_id
                }.lparams()
                imageButton {
                    id = R.id.tasks_item_id
                }.lparams()
                imageButton {
                    id = R.id.tasks_item_id
                }.lparams()
            }
        }
    }
}