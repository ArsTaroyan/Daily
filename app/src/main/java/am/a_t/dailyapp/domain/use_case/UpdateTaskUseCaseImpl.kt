package am.a_t.dailyapp.domain.use_case

import am.a_t.dailyapp.domain.iteractors.UpdateTaskUseCase
import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.domain.repo.Repository
import javax.inject.Inject

class UpdateTaskUseCaseImpl @Inject constructor(private val repository: Repository) :
    UpdateTaskUseCase {
    override suspend fun updateTask(task: Task) {
        repository.updateTask(task)
    }
}