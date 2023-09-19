package am.a_t.dailyapp.data.source.database

import am.a_t.dailyapp.domain.iteractors.TaskDao
import am.a_t.dailyapp.domain.iteractors.TodoDao
import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.domain.module.Todo
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [
        Todo::class,
        Task::class
    ],
    version = 1,
    exportSchema = false
)

abstract class ListDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
    abstract fun taskDao(): TaskDao
}