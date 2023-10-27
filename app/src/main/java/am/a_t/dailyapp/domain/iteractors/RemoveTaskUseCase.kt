package am.a_t.dailyapp.domain.iteractors

import am.a_t.dailyapp.domain.module.Task

interface RemoveTaskUseCase {
    suspend fun removeTask(task: Task)
}