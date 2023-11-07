package am.a_t.dailyapp.presentation.ui.todoFragment

import am.a_t.dailyapp.domain.iteractors.*
import am.a_t.dailyapp.domain.module.ListTodo
import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.domain.module.Todo
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
class TodoViewModel @Inject constructor(
    private val addTodoUseCase: AddTodoUseCase,
    private val getAllTodosUseCase: GetAllTodosUseCase,
    private val removeTodoUseCase: RemoveTodoUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase,
    private val getListUseCase: GetListUseCase
) : ViewModel(){

    // Todos

    val todoAllLiveData = MutableSharedFlow<Flow<List<Todo>>>(1)

    fun getAllTodo(id: Long) {
        viewModelScope.launch {
            todoAllLiveData.emit(getAllTodosUseCase.getAllTodos(id))
        }
    }

    fun addTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            addTodoUseCase.addTodo(todo)
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            updateTodoUseCase.updateTodo(todo)
        }
    }

    fun removeTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            removeTodoUseCase.removeTodo(todo)
        }
    }

    // List

    private val _getList: MutableStateFlow<ListTodo?> = MutableStateFlow(null)
    val getList = _getList.asSharedFlow()

    fun getList(id: Long) {
        viewModelScope.launch {
            _getList.emit(getListUseCase.getList(id))
        }
    }

    // Filter

    private val _filterTodo: MutableStateFlow<List<Todo?>?> = MutableStateFlow(null)
    val filterTodo = _filterTodo.asSharedFlow()

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
            if (newList.isEmpty()) {
                _filterTodo.value = null
            } else {
                _filterTodo.value = newList
            }
        }
    }
}