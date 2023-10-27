package am.a_t.dailyapp.domain.use_case

import am.a_t.dailyapp.domain.iteractors.RemoveTaskUseCase
import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.domain.repo.Repository
import javax.inject.Inject

class RemoveTaskUseCaseImpl @Inject constructor(private val repository: Repository) :
    RemoveTaskUseCase {
    override suspend fun removeTask(task: Task) {
        repository.removeTask(task)
    }
}