package am.a_t.dailyapp.domain.iteractors

import am.a_t.dailyapp.domain.module.Task
import kotlinx.coroutines.flow.Flow

interface GetAllTasksUseCase {
    fun getAllTasks(): Flow<List<Task>>
}