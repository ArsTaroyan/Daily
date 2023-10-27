package am.a_t.dailyapp.domain.module

import am.a_t.dailyapp.domain.utils.ListColor
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity("tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo("task_end_time") var taskEndTime: String,
    @ColumnInfo("task_calendar") var taskCalendar: Calendar?,
    @ColumnInfo("task_alarm") var taskIsAlarm: Boolean,
    @ColumnInfo("task_title") var taskTitle: String,
    @ColumnInfo("task_date") var taskDate: String,
    @ColumnInfo("task_color") var taskColor: ListColor,
    @ColumnInfo("task_description") var taskDescription: String
)