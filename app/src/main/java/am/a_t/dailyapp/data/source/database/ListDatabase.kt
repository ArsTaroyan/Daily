package am.a_t.dailyapp.data.source.database

import am.a_t.dailyapp.data.source.convert.Converters
import am.a_t.dailyapp.data.source.dao.ListTodoDao
import am.a_t.dailyapp.data.source.dao.TaskDao
import am.a_t.dailyapp.data.source.dao.TodoDao
import am.a_t.dailyapp.domain.module.ListTodo
import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.domain.module.Todo
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        Todo::class,
        Task::class,
        ListTodo::class
    ],
    version = 1,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class ListDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
    abstract fun taskDao(): TaskDao
    abstract fun listTodoDao(): ListTodoDao
}