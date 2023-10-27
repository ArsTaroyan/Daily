package am.a_t.dailyapp.domain.iteractors

import am.a_t.dailyapp.domain.module.Todo

interface AddTodoUseCase {
    suspend fun addTodo(todo: Todo)
}