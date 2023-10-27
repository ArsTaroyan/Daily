package am.a_t.dailyapp.domain.iteractors

import am.a_t.dailyapp.domain.module.Todo

interface GetTodoUseCase {
    suspend fun getTodo(id: Long): Todo?
}