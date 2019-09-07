/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.extensions

import android.widget.ImageView
import androidx.annotation.DrawableRes

fun ImageView.setImageXmlDrawable(@DrawableRes left: Int = 0) {
    setImageDrawable()
}