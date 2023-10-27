package am.a_t.dailyapp.domain.iteractors

import am.a_t.dailyapp.domain.module.ListTodo

interface AddListUseCase {
    suspend fun addList(list: ListTodo)
}