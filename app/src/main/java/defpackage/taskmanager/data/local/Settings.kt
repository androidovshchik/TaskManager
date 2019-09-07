/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.local

import com.skydoves.preferenceroom.KeyName
import com.skydoves.preferenceroom.PreferenceEntity
import com.skydoves.preferenceroom.PreferenceFunction

@PreferenceEntity("UserDevice")
open class Settings {

    @KeyName("version")
    @JvmField
    val deviceVersion: String? = null

    @KeyName("uuid")
    @JvmField
    val userUUID: String? = null

    @PreferenceFunction("uuid")
    open fun putUuidFunction(uuid: String?): String? {
        return SecurityUtils.encrypt(uuid)
    }

    @PreferenceFunction("uuid")
    open fun getUuidFunction(uuid: String?): String? {
        return SecurityUtils.decrypt(uuid)
    }
}