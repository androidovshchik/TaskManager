/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.local

import com.chibatching.kotpref.KotprefModel

object Preferences : KotprefModel() {

    override val kotprefName: String = "preferences"

    var pathToDb by nullableStringPref(null, "pathToDb")

    var enableTasksService by booleanPref(false, "enableTasksService")
}