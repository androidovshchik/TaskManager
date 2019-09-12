/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.tasks

import android.graphics.Color
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import defpackage.taskmanager.R
import defpackage.taskmanager.extensions.setImageXmlDrawable
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk19.listeners.onClick

class TasksActivityUI : AnkoComponent<TasksActivity> {

    override fun createView(ui: AnkoContext<TasksActivity>): View = with(ui) {
        verticalLayout {
            lparams(matchParent, matchParent)
            setPadding(0, dip(8), 0, dip(8))
            linearLayout {
                lparams(matchParent)
                setPadding(dip(8), 0, dip(8), dip(8))
                gravity = Gravity.CENTER_VERTICAL
                owner.etDbPath = editText {
                    maxLines = 1
                }.lparams(0) {
                    weight = 1f
                }
                imageButton {
                    setImageXmlDrawable(R.drawable.ic_folder_open)
                    onClick {
                        owner.onChooseDbFile()
                    }
                }.lparams()
                imageButton {
                    setImageXmlDrawable(R.drawable.ic_file_import)
                    onClick {
                        owner.onLoadDbFile()
                    }
                }.lparams()
                imageButton {
                    setImageXmlDrawable(R.drawable.ic_file_export)
                    onClick {
                        owner.onSaveDbFile()
                    }
                }.lparams()
            }
            view {
                background = ContextCompat.getDrawable(context, R.drawable.divider)
            }.lparams(matchParent, dip(1))
            frameLayout {
                id = TASKS_LAYOUT_ID
            }.lparams(matchParent, 0) {
                weight = 1f
            }
            view {
                background = ContextCompat.getDrawable(context, R.drawable.divider)
            }.lparams(matchParent, dip(1))
            linearLayout {
                lparams(matchParent)
                setPadding(dip(8), dip(8), dip(8), 0)
                gravity = Gravity.CENTER_VERTICAL
                owner.tvStatus = textView {
                    textSize = 15f
                    textColor = Color.BLACK
                    setPadding(dip(8), 0, dip(8), 0)
                }.lparams(0) {
                    weight = 1f
                }
                imageButton {
                    setImageXmlDrawable(R.drawable.ic_play_arrow_black_24dp)
                    onClick {
                        owner.onLaunchTasks()
                    }
                }.lparams()
                imageButton {
                    setImageXmlDrawable(R.drawable.ic_stop_black_24dp)
                    onClick {
                        owner.onKillTasks()
                    }
                }.lparams()
            }
        }
    }

    companion object {

        val TASKS_LAYOUT_ID = View.generateViewId()
    }
}