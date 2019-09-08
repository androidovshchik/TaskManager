/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.local

import android.content.Context
import com.chibatching.kotpref.KotprefModel

class Preferences(context: Context) : KotprefModel(context) {

    override val kotprefName: String = "preferences"

    var pathToDb by nullableStringPref(null, "pathToDb")

    var enableTasksService by booleanPref(false, "enableTasksService")
}