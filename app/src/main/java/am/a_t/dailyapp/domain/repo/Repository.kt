package am.a_t.dailyapp.domain.repo

import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.domain.module.Todo
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getAllTodos(): Flow<List<Todo>>

    suspend fun addTodo(todo: Todo)

    suspend fun removeTodo(todo: Todo)

    suspend fun updateTodo(todo: Todo)

    fun getAllTasks(): Flow<List<Task>>

    suspend fun addTask(task: Task)

    suspend fun removeTask(task: Task)

    suspend fun updateTask(task: Task)
}