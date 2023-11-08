package am.a_t.dailyapp.domain.use_case

import am.a_t.dailyapp.domain.iteractors.GetTaskUseCase
import am.a_t.dailyapp.domain.module.Task
import am.a_t.dailyapp.domain.repo.Repository
import javax.inject.Inject

class GetTaskUseCaseImpl @Inject constructor(private val repository: Repository) :
    GetTaskUseCase {
    override suspend fun getTask(id: Long): Task? = repository.getTask(id)
}