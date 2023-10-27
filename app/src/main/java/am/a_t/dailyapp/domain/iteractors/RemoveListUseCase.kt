package am.a_t.dailyapp.domain.iteractors

import am.a_t.dailyapp.domain.module.ListTodo

interface RemoveListUseCase {
    suspend fun removeList(listTodo: ListTodo)
}