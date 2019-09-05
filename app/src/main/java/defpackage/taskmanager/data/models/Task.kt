/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "задачи")
class Task : Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    var id = 0L

    @ColumnInfo(name = "Название")
    lateinit var title: String

    @ColumnInfo(name = "[Т-Время]")
    var tTime: Date? = null

    @ColumnInfo(name = "[Т-День]")
    var tDay: Day? = null

    @ColumnInfo(name = "[Т-Дата]")
    var tDate: Date? = null

    @ColumnInfo(name = "[Т-Задача]")
    var tTask: Long? = null

    @ColumnInfo(name = "[Т-Повторы]")
    var tRepeat: Int? = null

    @ColumnInfo(name = "[Т-Задержка]")
    var tDelay: Long? = null

    @ColumnInfo(name = "Сигнал")
    lateinit var signal: Signal

    @ColumnInfo(name = "[Интервал повторения]")
    var iRepeat = INTERVAL_REPEAT

    @ColumnInfo(name = "[Интервал откладывания]")
    var iDelay = INTERVAL_DELAY

    @ColumnInfo(name = "Статус")
    var status = false

    companion object {

        const val INTERVAL_REPEAT = 10L

        const val INTERVAL_DELAY = 300L
    }
}