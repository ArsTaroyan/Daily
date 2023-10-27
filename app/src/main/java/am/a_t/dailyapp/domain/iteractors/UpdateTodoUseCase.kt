package am.a_t.dailyapp.domain.iteractors

import am.a_t.dailyapp.domain.module.Todo

interface UpdateTodoUseCase {
    suspend fun updateTodo(todo: Todo)
}