package am.a_t.dailyapp.domain.use_case

import am.a_t.dailyapp.domain.iteractors.AddTodoUseCase
import am.a_t.dailyapp.domain.module.Todo
import am.a_t.dailyapp.domain.repo.Repository
import javax.inject.Inject

class AddTodoUseCaseImpl @Inject constructor(private val repository: Repository) : AddTodoUseCase {
    override suspend fun addTodo(todo: Todo) {
        repository.addTodo(todo)
    }
}