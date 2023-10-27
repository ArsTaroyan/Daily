package am.a_t.dailyapp.domain.iteractors

import am.a_t.dailyapp.domain.module.ListTodo

interface UpdateListUseCase {
    suspend fun updateList(listTodo: ListTodo)
}