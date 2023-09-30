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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    val todoAllLiveData = MutableSharedFlow<Flow<List<Todo>>>(1)
    val taskAllLiveData = MutableSharedFlow<Flow<List<Task>>>(1)

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

    fun getTodo(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getTask(id)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(task)
        }
    }

    fun getTask(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getTask(id)
        }
    }
}