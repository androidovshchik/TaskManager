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

    @ColumnInfo(name = "[Т-Задержка]")
    var tDelay: Long? = null
}
CREATE TABLE задачи (
ID                      INTEGER NOT NULL
PRIMARY KEY AUTOINCREMENT,
Название                VARCHAR NOT NULL
DEFAULT "",
[Т-Время]               TIME,
[Т-День]                INTEGER REFERENCES неделя (ID) ON DELETE SET NULL
ON UPDATE CASCADE,
[Т-Дата]                DATE,
[Т-Задача]              INTEGER REFERENCES задачи (ID) ON DELETE SET NULL
ON UPDATE CASCADE,
[Т-Повторы]             INTEGER,
[Т-Задержка]            INTEGER,
Сигнал                  INTEGER NOT NULL
DEFAULT (1)
REFERENCES сигналы (ID) ON DELETE SET DEFAULT
ON UPDATE CASCADE,
[Интервал повторения]   INTEGER NOT NULL
DEFAULT (10),
[Интервал откладывания] INTEGER NOT NULL
DEFAULT (300),
Статус                  BOOLEAN NOT NULL
DEFAULT (0)
)
