package am.a_t.dailyapp.domain.use_case

import am.a_t.dailyapp.domain.iteractors.GetAllTasksUseCase
import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.domain.repo.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTasksUseCaseImpl @Inject constructor(private val repository: Repository) :
    GetAllTasksUseCase {
    override fun getAllTasks(): Flow<List<Task>> = repository.getAllTasks()
}