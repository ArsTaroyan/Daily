package am.a_t.dailyapp.domain.iteractors

import am.a_t.dailyapp.domain.module.Task

interface AddTaskUseCase {
    suspend fun addTask(task: Task)
}