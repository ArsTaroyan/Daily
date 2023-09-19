package am.a_t.dailyapp.domain.repo

import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.domain.module.Todo
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getAllTodos(): Flow<List<Todo>>
    suspend fun addTodo(todo: Todo)
    suspend fun deleteTodo(todo: Todo)

    suspend fun getAllTasks(): Flow<List<Task>>
    suspend fun addTask(task: Task)
    suspend fun deleteTask(task: Task)
}