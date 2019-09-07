/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

@file:Suppress("DEPRECATION")

package defpackage.taskmanager.screens

import android.app.Fragment
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinTrigger
import org.kodein.di.android.kodein

open class BaseFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    override val kodeinTrigger = KodeinTrigger()
}