package am.a_t.dailyapp.domain.use_case

import am.a_t.dailyapp.domain.iteractors.GetListUseCase
import am.a_t.dailyapp.domain.module.ListTodo
import am.a_t.dailyapp.domain.repo.Repository
import javax.inject.Inject

class GetListUseCaseImpl @Inject constructor(private val repository: Repository) :
    GetListUseCase {
    override suspend fun getList(id: Long): ListTodo? = repository.getList(id)
}