package am.a_t.dailyapp.domain.module

import am.a_t.dailyapp.utils.ListColor
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("todos")
data class Todo(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo("todo_is_checked") val todoIsChecked: Boolean,
    @ColumnInfo("todo_title") val todoTitle: String,
    @ColumnInfo("todo_date") val todoDate: String,
    @ColumnInfo("todo_color") val todoColor: ListColor?
)