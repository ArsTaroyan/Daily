package am.a_t.dailyapp.presentation.ui.createNewTask

import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.domain.repo.Repository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateNewTaskViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(task)
        }
    }
}