/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.widgets

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import defpackage.taskmanager.R
import defpackage.taskmanager.data.models.Task
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import java.util.*

class TasksWidgetService : RemoteViewsService(), KodeinAware {

    override val kodein by kodein()

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory = Factory(
        this, intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
    )

    class Factory(private val context: Context, private val appWidgetId: Int) : RemoteViewsFactory {

        private var tasks: List<Task> = ArrayList()
        private var color = 0

        override fun onDataSetChanged() {
            var list = context.getList(appWidgetId)
            list = TrelloAPIUtil.instance.getCards(list)
            color = context.getCardForegroundColor()

            if (BoardList.ERROR != list.id) {
                tasks = list.cards
            } else {
                color = color.dim()
            }
        }

        override fun getViewAt(position: Int): RemoteViews {
            val item = tasks[position]
            val views = RemoteViews(context.packageName, R.layout.item_task)
            return views
        }

        override fun onCreate() {
        }

        override fun onDestroy() {
        }

        override fun getCount(): Int = tasks.size

        override fun getLoadingView(): RemoteViews? = null

        override fun getViewTypeCount(): Int = 1

        override fun getItemId(position: Int): Long = position.toLong()

        override fun hasStableIds() = true
    }
}