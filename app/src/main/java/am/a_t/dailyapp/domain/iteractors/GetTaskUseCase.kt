package am.a_t.dailyapp.domain.iteractors

import am.a_t.dailyapp.domain.module.Task

interface GetTaskUseCase {
    suspend fun getTask(id: Long): Task?
}