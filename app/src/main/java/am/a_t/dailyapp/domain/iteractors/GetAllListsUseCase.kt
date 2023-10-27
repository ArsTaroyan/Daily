package am.a_t.dailyapp.domain.iteractors

import am.a_t.dailyapp.domain.module.ListTodo
import kotlinx.coroutines.flow.Flow

interface GetAllListsUseCase {
    fun getAllLists(): Flow<List<ListTodo>>
}