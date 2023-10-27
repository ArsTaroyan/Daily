package am.a_t.dailyapp.domain.use_case

import am.a_t.dailyapp.domain.iteractors.GetAllListsUseCase
import am.a_t.dailyapp.domain.module.ListTodo
import am.a_t.dailyapp.domain.repo.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllListsUseCaseImpl @Inject constructor(private val repository: Repository) :
    GetAllListsUseCase {
    override fun getAllLists(): Flow<List<ListTodo>> = repository.getAllLists()
}