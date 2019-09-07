/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.extensions

import android.widget.ImageView
import androidx.annotation.DrawableRes

fun ImageView.setImageXmlDrawable(@DrawableRes id: Int = 0) {
    if (isLollipopPlus()) {
        setImageResource(id)
    } else {
        setImageDrawable(context.createXmlDrawable(id))
    }
}