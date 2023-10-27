package am.a_t.dailyapp.domain.iteractors

import am.a_t.dailyapp.domain.module.Task

interface UpdateTaskUseCase {
    suspend fun updateTask(task: Task)
}