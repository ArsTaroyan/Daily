package am.a_t.dailyapp.domain.use_case

import am.a_t.dailyapp.domain.iteractors.UpdateTodoUseCase
import am.a_t.dailyapp.domain.module.Todo
import am.a_t.dailyapp.domain.repo.Repository
import javax.inject.Inject

class UpdateTodoUseCaseImpl @Inject constructor(private val repository: Repository) :
    UpdateTodoUseCase {
    override suspend fun updateTodo(todo: Todo) {
        repository.updateTodo(todo)
    }
}