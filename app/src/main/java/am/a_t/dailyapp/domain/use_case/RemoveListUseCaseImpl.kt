package am.a_t.dailyapp.domain.use_case

import am.a_t.dailyapp.domain.iteractors.RemoveListUseCase
import am.a_t.dailyapp.domain.module.ListTodo
import am.a_t.dailyapp.domain.repo.Repository
import javax.inject.Inject

class RemoveListUseCaseImpl @Inject constructor(private val repository: Repository) :
    RemoveListUseCase {
    override suspend fun removeList(listTodo: ListTodo) {
        repository.removeList(listTodo)
    }
}