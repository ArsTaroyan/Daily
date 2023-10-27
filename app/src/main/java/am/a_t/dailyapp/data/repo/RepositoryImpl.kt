package am.a_t.dailyapp.data.repo

import am.a_t.dailyapp.data.source.dao.ListTodoDao
import am.a_t.dailyapp.data.source.dao.TaskDao
import am.a_t.dailyapp.data.source.dao.TodoDao
import am.a_t.dailyapp.domain.module.ListTodo
import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.domain.module.Todo
import am.a_t.dailyapp.domain.repo.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val todoDao: TodoDao,
    private val taskDao: TaskDao,
    private val listTodoDao: ListTodoDao
) : Repository {

    // Tasks

    override fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    override suspend fun getTask(id: Long): Task? = taskDao.getTask(id)

    override suspend fun addTask(task: Task) {
        taskDao.addTask(task)
    }

    override suspend fun removeTask(task: Task) {
        taskDao.removeTask(task)
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    // Todos

    override fun getAllTodos(id: Long): Flow<List<Todo>> = todoDao.getAllTodos(id)

    override suspend fun getTodo(id: Long): Todo? = todoDao.getTodo(id)

    override suspend fun addTodo(todo: Todo) {
        todoDao.addTodo(todo)
    }

    override suspend fun removeTodo(todo: Todo) {
        todoDao.removeTodo(todo)
    }

    override suspend fun updateTodo(todo: Todo) {
        todoDao.updateTodo(todo)
    }

    // Lists

    override fun getAllLists(): Flow<List<ListTodo>> = listTodoDao.getAllLists()

    override suspend fun getList(id: Long): ListTodo? = listTodoDao.getList(id)

    override suspend fun addList(listTodo: ListTodo) {
        listTodoDao.addList(listTodo)
    }

    override suspend fun removeList(listTodo: ListTodo) {
        listTodoDao.removeList(listTodo)
    }

    override suspend fun updateList(listTodo: ListTodo) {
        listTodoDao.updateList(listTodo)
    }
}