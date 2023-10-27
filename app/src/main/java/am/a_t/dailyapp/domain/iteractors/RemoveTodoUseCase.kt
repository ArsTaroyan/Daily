package am.a_t.dailyapp.domain.iteractors

import am.a_t.dailyapp.domain.module.Todo

interface RemoveTodoUseCase {
    suspend fun removeTodo(todo: Todo)
}