package am.a_t.dailyapp.domain.repo

import am.a_t.dailyapp.domain.module.ListTodo
import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.domain.module.Todo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface Repository{

    // Todos

    fun getAllTodos(id: Long): Flow<List<Todo>>

    suspend fun getTodo(id: Long): Todo?

    suspend fun addTodo(todo: Todo)

    suspend fun removeTodo(todo: Todo)

    suspend fun updateTodo(todo: Todo)

    // Tasks

    fun getAllTasks(): Flow<List<Task>>

    suspend fun getTask(id: Long): Task?

    suspend fun addTask(task: Task)

    suspend fun removeTask(task: Task)

    suspend fun updateTask(task: Task)

    // Lists

    fun getAllLists(): Flow<List<ListTodo>>

    suspend fun getList(id: Long): ListTodo?

    suspend fun addList(listTodo: ListTodo)

    suspend fun removeList(listTodo: ListTodo)

    suspend fun updateList(listTodo: ListTodo)
}