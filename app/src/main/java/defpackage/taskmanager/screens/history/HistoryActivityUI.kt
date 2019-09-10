/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.history

import android.view.View
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent

class HistoryActivityUI : AnkoComponent<HistoryActivity> {

    override fun createView(ui: AnkoContext<HistoryActivity>): View = with(ui) {
        frameLayout {
            id = FRAME_LAYOUT_ID
        }.lparams(matchParent, 0) {
            weight = 1f
        }
    }

    companion object {

        @JvmStatic
        val HISTORY_LAYOUT_ID = View.generateViewId()
    }
}