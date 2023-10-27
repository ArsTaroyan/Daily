package am.a_t.dailyapp.domain.use_case

import am.a_t.dailyapp.domain.iteractors.GetTodoUseCase
import am.a_t.dailyapp.domain.module.Todo
import am.a_t.dailyapp.domain.repo.Repository
import javax.inject.Inject

class GetTodoUseCaseImpl @Inject constructor(private val repository: Repository) :
    GetTodoUseCase {
    override suspend fun getTodo(id: Long): Todo? = repository.getTodo(id)
}