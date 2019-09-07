/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.tasks

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import org.jetbrains.anko.*

class TasksAdapterUI : AnkoComponent<ViewGroup> {

    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        verticalLayout {
            lparams(matchParent, wrapContent)
            padding = dip(8)

            textView {
                id = tvTitleId
                layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)
                text = "Sherlock"
                textSize = 16f // <- it is sp, no worries
                textColor = Color.BLACK
            }

            textView {
                id = tvYearId
                layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)
                text = "2009"
                textSize = 14f
            }
        }
    }
}