package am.a_t.dailyapp.domain.use_case

import am.a_t.dailyapp.domain.iteractors.AddTaskUseCase
import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.domain.repo.Repository
import javax.inject.Inject

class AddTaskUseCaseImpl @Inject constructor(private val repository: Repository) : AddTaskUseCase {
    override suspend fun addTask(task: Task) {
        repository.addTask(task)
    }
}