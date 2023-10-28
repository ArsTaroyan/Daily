package am.a_t.dailyapp.domain.module

import am.a_t.dailyapp.domain.utils.ListColor
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("lists")
data class ListTodo(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo("list_is_checked") var listIsChecked: Boolean,
    @ColumnInfo("list_title") var listTitle: String,
    @ColumnInfo("list_date") var listDate: String,
    @ColumnInfo("list_color") var listColor: ListColor,
    @ColumnInfo(name = "list_todo") val listTodo: ArrayList<Todo>?
)