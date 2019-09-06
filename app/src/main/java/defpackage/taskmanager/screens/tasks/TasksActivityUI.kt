/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.tasks

import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk19.listeners.onClick

class TasksActivityUI : AnkoComponent<TasksActivity> {

    override fun createView(ui: AnkoContext<TasksActivity>): View = with(ui) {
        verticalLayout {
            lparams(width = matchParent, height = matchParent)
            padding = dip(8)
            linearLayout {
                lparams(width = matchParent)
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
            owner.recyclerView = recyclerView().lparams(width = matchParent, height = 0) {
                weight = 1f
            }
            linearLayout {
                lparams(width = matchParent)
                button("Запуск обработки задач") {
                    onClick {
                        owner.onChooseDbFile()
                    }
                }.lparams(width = 0) {
                    weight = 1f
                }
                button("Остановить все") {
                    onClick {
                        owner.onLoadTasksFromDbFile()
                    }
                }.lparams(width = 0) {
                    weight = 1f
                }
            }
        }
    }
}