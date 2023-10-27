package am.a_t.dailyapp.domain.use_case

import am.a_t.dailyapp.domain.iteractors.GetAllTodosUseCase
import am.a_t.dailyapp.domain.module.Todo
import am.a_t.dailyapp.domain.repo.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTodosUseCaseImpl @Inject constructor(private val repository: Repository) :
    GetAllTodosUseCase {
    override fun getAllTodos(id: Long): Flow<List<Todo>> = repository.getAllTodos(id)
}