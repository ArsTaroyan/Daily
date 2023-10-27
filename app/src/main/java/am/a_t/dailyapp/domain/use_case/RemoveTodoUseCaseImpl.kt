package am.a_t.dailyapp.domain.use_case

import am.a_t.dailyapp.domain.iteractors.RemoveTodoUseCase
import am.a_t.dailyapp.domain.module.Todo
import am.a_t.dailyapp.domain.repo.Repository
import javax.inject.Inject

class RemoveTodoUseCaseImpl @Inject constructor(private val repository: Repository) :
    RemoveTodoUseCase {
    override suspend fun removeTodo(todo: Todo) {
        repository.removeTodo(todo)
    }
}