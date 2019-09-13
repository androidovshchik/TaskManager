/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package defpackage.taskmanager.services

import android.content.Context
import android.os.VibrationEffect
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import defpackage.taskmanager.R
import defpackage.taskmanager.data.models.Behavior
import defpackage.taskmanager.extensions.isOreoPlus
import org.jetbrains.anko.vibrator

class BehaviorManager(context: Context) {

    private val soundPlayer = ExoPlayerFactory.newSimpleInstance(context)

    private val soundFactory = ProgressiveMediaSource.Factory(DataSource.Factory {
        RawResourceDataSource(context)
    })

    private val soundSource = soundFactory.createMediaSource(
        RawResourceDataSource.buildRawResourceUri(R.raw.sound)
    )

    init {
        soundPlayer.prepare(soundSource)
    }

    fun ssfsf(context: Context, behavior: Behavior) {

    }

    @Suppress("DEPRECATION")
    private fun vibrate(context: Context) = context.run {
        if (isOreoPlus()) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(500)
        }
    }

    private fun playSound(id: Int) {
        stopPlay()
        soundPlayer.apply {
            prepare(
                when (id) {
                    R.raw.sound -> soundSource
                    else -> soundFactory.createMediaSource(
                        RawResourceDataSource.buildRawResourceUri(id)
                    )
                }
            )
            playWhenReady = true
        }
    }

    fun stopPlay() {
        soundPlayer.playWhenReady = false
    }

    fun release() {
        stopPlay()
        soundPlayer.apply {
            stop()
            release()
        }
    }
}
