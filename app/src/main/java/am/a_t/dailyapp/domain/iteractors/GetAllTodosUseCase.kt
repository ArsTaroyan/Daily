package am.a_t.dailyapp.domain.iteractors

import am.a_t.dailyapp.domain.module.Todo
import kotlinx.coroutines.flow.Flow

interface GetAllTodosUseCase {
    fun getAllTodos(id: Long): Flow<List<Todo>>
}