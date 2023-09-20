package am.a_t.dailyapp.data.repo

import am.a_t.dailyapp.domain.iteractors.TaskDao
import am.a_t.dailyapp.domain.iteractors.TodoDao
import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.domain.module.Todo
import am.a_t.dailyapp.domain.repo.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val todoDao: TodoDao,
    private val taskDao: TaskDao
) : Repository {
    override fun getAllTodos(): Flow<List<Todo>> = todoDao.getAllTodos()

    override suspend fun addTodo(todo: Todo) {
        todoDao.addTodo(todo)
    }

    override suspend fun removeTodo(todo: Todo) {
        todoDao.removeTodo(todo)
    }

    override suspend fun updateTodo(todo: Todo) {
        todoDao.updateTodo(todo)
    }

    override fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    override suspend fun addTask(task: Task) {
        taskDao.addTask(task)
    }

    override suspend fun removeTask(task: Task) {
        taskDao.removeTask(task)
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }
}