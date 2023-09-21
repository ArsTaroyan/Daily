package am.a_t.dailyapp.domain.module

import am.a_t.dailyapp.utils.ListColor
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo("task_end_time") val taskEndTime: String,
    @ColumnInfo("task_title") val taskTitle: String,
    @ColumnInfo("task_date") val taskDate: String,
    @ColumnInfo("task_color") val taskColor: ListColor,
    @ColumnInfo("task_description") val taskDescription: String
)