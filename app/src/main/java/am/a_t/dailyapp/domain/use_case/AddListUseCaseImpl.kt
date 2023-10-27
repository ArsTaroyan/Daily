package am.a_t.dailyapp.domain.use_case

import am.a_t.dailyapp.domain.iteractors.AddListUseCase
import am.a_t.dailyapp.domain.module.ListTodo
import am.a_t.dailyapp.domain.repo.Repository
import javax.inject.Inject

class AddListUseCaseImpl @Inject constructor(private val repository: Repository) : AddListUseCase {
    override suspend fun addList(listTodo: ListTodo) {
        repository.addList(listTodo)
    }
}