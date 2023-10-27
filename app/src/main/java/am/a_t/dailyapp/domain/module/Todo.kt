package am.a_t.dailyapp.domain.module

import am.a_t.dailyapp.domain.utils.ListColor
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("todos")
data class Todo(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo("list_id") var list_id: Long,
    @ColumnInfo("todo_is_checked") var todoIsChecked: Boolean,
    @ColumnInfo("todo_title") var todoTitle: String,
    @ColumnInfo("todo_date") var todoDate: String,
    @ColumnInfo("todo_color") var todoColor: ListColor?
)