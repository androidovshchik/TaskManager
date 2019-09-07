/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.tasks

import android.graphics.Color
import android.view.Gravity
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk19.listeners.onClick

class TasksActivityUI : AnkoComponent<TasksActivity> {

    override fun createView(ui: AnkoContext<TasksActivity>): View = with(ui) {
        verticalLayout {
            lparams(width = matchParent, height = matchParent)
            padding = dip(8)
            linearLayout {
                lparams(width = matchParent)
                gravity = Gravity.CENTER_VERTICAL
                editText {

                }.lparams(width = 0) {
                    weight = 1f
                }
                button("Обзор") {
                    onClick {
                        owner.onChooseDbFile()
                    }
                }.lparams()
                button("Загрузить задачи") {
                    onClick {
                        owner.onLoadTasksFromDbFile()
                    }
                }.lparams()
            }
            owner.tvInfo = textView {
                textSize = 15f
                textColor = Color.BLACK
            }.lparams(width = matchParent)
            frameLayout().lparams(width = matchParent, height = 0) {
                id = FRAME_LAYOUT_ID
                weight = 1f
            }
            linearLayout {
                lparams(width = matchParent)
                gravity = Gravity.CENTER_VERTICAL
                button("Запуск обработки задач") {
                    onClick {
                        owner.onLaunchService()
                    }
                }.lparams(width = 0) {
                    weight = 1f
                }
                button("Остановить все") {
                    onClick {
                        owner.onStopAllTasks()
                    }
                }.lparams(width = 0) {
                    weight = 1f
                }
            }
            owner.tvStatus = textView {
                textSize = 15f
                textColor = Color.BLACK
            }.lparams(width = matchParent)
        }
    }

    companion object {

        @JvmStatic
        val FRAME_LAYOUT_ID = View.generateViewId()
    }
}