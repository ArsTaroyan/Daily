package am.a_t.dailyapp.domain.use_case

import am.a_t.dailyapp.domain.iteractors.UpdateListUseCase
import am.a_t.dailyapp.domain.module.ListTodo
import am.a_t.dailyapp.domain.repo.Repository
import javax.inject.Inject

class UpdateListUseCaseImpl @Inject constructor(private val repository: Repository) :
    UpdateListUseCase {
    override suspend fun updateList(listTodo: ListTodo) {
        repository.updateList(listTodo)
    }
}