package am.a_t.dailyapp.presentation.ui.mainFragment

import am.a_t.dailyapp.domain.iteractors.*
import am.a_t.dailyapp.domain.module.ListTodo
import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.domain.module.Todo
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    private val addTodoUseCase: AddTodoUseCase,
    private val addListUseCase: AddListUseCase,
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val getAllTodosUseCase: GetAllTodosUseCase,
    private val getAllListsUseCase: GetAllListsUseCase,
    private val removeTaskUseCase: RemoveTaskUseCase,
    private val removeTodoUseCase: RemoveTodoUseCase,
    private val removeListUseCase: RemoveListUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase,
    private val updateListUseCase: UpdateListUseCase,
    private val getTaskUseCase: GetTaskUseCase,
    private val getTodoUseCase: GetTodoUseCase,
    private val getListUseCase: GetListUseCase
) : ViewModel() {

    // Tasks

    val taskAllLiveData = MutableSharedFlow<Flow<List<Task>>>(1)

    private val _getTask: MutableStateFlow<Task?> = MutableStateFlow(null)
    val getTask = _getTask.asSharedFlow()

    fun getAllTask() {
        viewModelScope.launch {
            taskAllLiveData.emit(getAllTasksUseCase.getAllTasks())
        }
    }

    fun getTask(id: Long) {
        viewModelScope.launch {
            _getTask.emit(getTaskUseCase.getTask(id))
        }
    }

    fun removeTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            removeTaskUseCase.removeTask(task)
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            addTaskUseCase.addTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            updateTaskUseCase.updateTask(task)
        }
    }

    // Lists

    val listAllLiveData = MutableSharedFlow<Flow<List<ListTodo>>>(1)

    private val _getList: MutableStateFlow<ListTodo?> = MutableStateFlow(null)
    val getList = _getList.asSharedFlow()

    fun getAllList() {
        viewModelScope.launch {
            listAllLiveData.emit(getAllListsUseCase.getAllLists())
        }
    }

    fun getList(id: Long) {
        viewModelScope.launch {
            _getList.emit(getListUseCase.getList(id))
        }
    }

    fun addList(listTodo: ListTodo) {
        viewModelScope.launch(Dispatchers.IO) {
            addListUseCase.addList(listTodo)
        }
    }

    fun updateList(listTodo: ListTodo) {
        viewModelScope.launch(Dispatchers.IO) {
            updateListUseCase.updateList(listTodo)
        }
    }

    fun removeList(listTodo: ListTodo) {
        viewModelScope.launch(Dispatchers.IO) {
            removeListUseCase.removeList(listTodo)
        }
    }

    // Filters

    private val _filterTask: MutableStateFlow<List<Task?>?> = MutableStateFlow(null)
    val filterTask = _filterTask.asSharedFlow()

    private val _filterList: MutableStateFlow<List<ListTodo?>?> = MutableStateFlow(null)
    val filterList = _filterList.asSharedFlow()

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

            if (newList.isEmpty()) {
                _filterTask.value = null
            } else {
                _filterTask.value = newList
            }
        }
    }

    fun getListTodoFilter(listTodos: List<ListTodo>, date: String?) {
        if (date.isNullOrEmpty()) {
            _filterList.value = listTodos
        } else {
            val newList = mutableListOf<ListTodo?>()
            for (i in listTodos.indices) {
                if (listTodos[i].listDate == date) {
                    newList.add(listTodos[i])
                }
            }
            if (newList.isEmpty()) {
                _filterList.value = null
            } else {
                _filterList.value = newList
            }
        }
    }

}