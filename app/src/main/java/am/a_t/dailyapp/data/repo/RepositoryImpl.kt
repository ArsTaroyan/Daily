package am.a_t.dailyapp.data.repo

import am.a_t.dailyapp.domain.iteractors.TaskDao
import am.a_t.dailyapp.domain.iteractors.TodoDao
import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.domain.module.Todo
import am.a_t.dailyapp.domain.repo.Repository
import kotlinx.coroutines.flow.Flow

class RepositoryImpl(private val todoDao: TodoDao, private val taskDao: TaskDao): Repository {
    override suspend fun getAllTodos(): Flow<List<Todo>> = todoDao.getAllTodos()

    override suspend fun addTodo(todo: Todo) {
        todoDao.addTodo(todo)
    }

    override suspend fun deleteTodo(todo: Todo) {
        todoDao.deleteTodo(todo)
    }

    override suspend fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    override suspend fun addTask(task: Task) {
        taskDao.addTask(task)
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }
}