/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.screens.history

import android.content.Context
import android.os.Bundle
import defpackage.taskmanager.EXTRA_ID
import defpackage.taskmanager.EXTRA_TITLE
import defpackage.taskmanager.screens.BaseActivity
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.setContentView

class HistoryActivity : BaseActivity() {

    private lateinit var historyFragment: HistoryFragment

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HistoryActivityUI().setContentView(this)
        title = intent.getStringExtra(EXTRA_TITLE)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        historyFragment = HistoryFragment.newInstance(intent.getLongExtra(EXTRA_ID, 0L))
        fragmentManager.beginTransaction()
            .add(HistoryActivityUI.HISTORY_LAYOUT_ID, historyFragment)
            .commit()
    }

    companion object {

        fun launch(context: Context, id: Long, title: String) = context.run {
            startActivity(
                intentFor<HistoryActivity>(
                    EXTRA_ID to id,
                    EXTRA_TITLE to title
                )
            )
        }
    }
}
