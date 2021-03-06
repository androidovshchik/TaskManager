/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package defpackage.taskmanager.services

import android.content.Context
import android.os.VibrationEffect
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import defpackage.taskmanager.R
import defpackage.taskmanager.data.models.Behavior
import defpackage.taskmanager.extensions.isOreoPlus
import org.jetbrains.anko.vibrator

class BehaviorManager(context: Context) {

    private val soundPlayer = ExoPlayerFactory.newSimpleInstance(context).apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .setUsage(C.USAGE_MEDIA)
                .setFlags(C.FLAG_AUDIBILITY_ENFORCED)
                .build(), false
        )
    }

    private val soundFactory = ProgressiveMediaSource.Factory(DataSource.Factory {
        RawResourceDataSource(context)
    })

    private val soundSource = soundFactory.createMediaSource(
        RawResourceDataSource.buildRawResourceUri(R.raw.sound)
    )

    init {
        soundPlayer.prepare(soundSource)
    }

    fun makeNoise(context: Context, behavior: Behavior) = context.run {
        when (behavior) {
            Behavior.SOUNDLESS -> {
            }
            Behavior.VIBRATION -> vibrate(context)
            Behavior.SOUND -> playSound(R.raw.sound)
            Behavior.SOUND_VIBRATION -> {
                playSound(R.raw.sound)
                vibrate(context)
            }
            Behavior.DEFAULT -> {
                // todo
            }
        }
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
