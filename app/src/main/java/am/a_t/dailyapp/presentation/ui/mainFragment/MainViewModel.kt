package am.a_t.dailyapp.presentation.ui.mainFragment

import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.domain.module.Todo
import am.a_t.dailyapp.domain.repo.Repository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    val todoAllLiveData = MutableSharedFlow<Flow<List<Todo>>>(1)
    val taskAllLiveData = MutableSharedFlow<Flow<List<Task>>>(1)

    private val _filterTodo: MutableStateFlow<List<Todo?>?> = MutableStateFlow(null)
    val filterTodo = _filterTodo.asSharedFlow()

    private val _filterTask: MutableStateFlow<List<Task?>?> = MutableStateFlow(null)
    val filterTask = _filterTask.asSharedFlow()

    fun getAllTodo() {
        viewModelScope.launch {
            todoAllLiveData.emit(repository.getAllTodos())
        }
    }

    fun getAllTask() {
        viewModelScope.launch {
            taskAllLiveData.emit(repository.getAllTasks())
        }
    }

    fun addTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTodo(todo)
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTodo(todo)
        }
    }

    fun removeTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeTodo(todo)
        }
    }

    fun removeTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeTask(task)
        }
    }

    fun getTodoFilter(todos: List<Todo>, date: String?) {

        if (date.isNullOrEmpty()) {
            _filterTodo.value = todos
        } else {
            val newList = mutableListOf<Todo?>()
            for (i in todos.indices) {
                if (todos[i].todoDate == date) {
                    newList.add(todos[i])
                }
            }
            _filterTodo.value = newList
        }

    }

    fun getTaskFilter(tasks: List<Task>, date: String?) {

        if (date.isNullOrEmpty()) {
            _filterTask.value = tasks
        } else {
            val newList = mutableListOf<Task?>()
            for (i in tasks.indices) {
                if (tasks[i].taskDate == date) {
                    newList.add(tasks[i])
                }
            }
            _filterTask.value = newList
        }

    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(task)
        }
    }
}