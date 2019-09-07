/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.local

import com.chibatching.kotpref.KotprefModel

object Preferences : KotprefModel() {

    var enabledTasksService by booleanPref(false, "enabledTasksService")
}