/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.history

import android.os.Bundle
import defpackage.taskmanager.EXTRA_ID
import defpackage.taskmanager.screens.BaseActivity
import org.jetbrains.anko.setContentView

class HistoryActivity : BaseActivity() {

    private lateinit var historyFragment: HistoryFragment

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HistoryActivityUI().setContentView(this)
        historyFragment = HistoryFragment.newInstance(intent.getLongExtra(EXTRA_ID, 0L))
        fragmentManager.beginTransaction()
            .add(HistoryActivityUI.HISTORY_LAYOUT_ID, historyFragment)
            .commit()
    }
}
