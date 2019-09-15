/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

@file:Suppress("DEPRECATION")

package defpackage.taskmanager.screens.tasks

import android.app.FragmentManager
import android.view.ViewGroup
import androidx.collection.SimpleArrayMap
import androidx.legacy.app.FragmentStatePagerAdapter
import defpackage.taskmanager.screens.BaseFragment
import defpackage.taskmanager.screens.events.EventsFragment

class TasksPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {

    val fragments = SimpleArrayMap<Int, BaseFragment>()

    override fun getItem(position: Int): BaseFragment {
        var fragment: BaseFragment? = null
        if (!fragments.containsKey(position)) {
            fragment = when (position) {
                0 -> TasksFragment()
                else -> EventsFragment()
            }
            fragments.put(position, fragment)
        }
        return fragments.getOrDefault(position, fragment)
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? = null

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
        fragments.remove(position)
    }
}