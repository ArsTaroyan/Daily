package am.a_t.dailyapp.di

import am.a_t.dailyapp.data.repo.RepositoryImpl
import am.a_t.dailyapp.data.source.dao.ListTodoDao
import am.a_t.dailyapp.data.source.dao.TaskDao
import am.a_t.dailyapp.data.source.dao.TodoDao
import am.a_t.dailyapp.data.source.database.ListDatabase
import am.a_t.dailyapp.domain.iteractors.*
import am.a_t.dailyapp.domain.repo.Repository
import am.a_t.dailyapp.domain.use_case.*
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Module {

    @Provides
    @Singleton
    fun providesContext(@ApplicationContext context: Context) = context

    @Provides
    @Singleton
    fun providesRepository(context: Context): Repository =
        RepositoryImpl(
            getDatabase(context).todoDao(),
            getDatabase(context).taskDao(),
            getDatabase(context).listTodoDao()
        )

    @Volatile
    private var mINSTANCE: ListDatabase? = null
    private fun getDatabase(context: Context): ListDatabase {
        val tempInstance = mINSTANCE
        if (tempInstance != null) {
            return tempInstance
        }
        synchronized(context.applicationContext) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                ListDatabase::class.java,
                "list_database"
            ).build()
            mINSTANCE = instance
            return instance
        }
    }

    // Todos

    @Provides
    @Singleton
    fun providesTodoDao(context: Context): TodoDao = getDatabase(context).todoDao()

    @Provides
    @Singleton
    fun providesAddTodoUseCase(repository: Repository): AddTodoUseCase {
        return AddTodoUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesGetAllTodosUseCase(repository: Repository): GetAllTodosUseCase {
        return GetAllTodosUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesRemoveTodoUseCase(repository: Repository): RemoveTodoUseCase {
        return RemoveTodoUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesUpdateTodoUseCase(repository: Repository): UpdateTodoUseCase {
        return UpdateTodoUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesGetTodoUseCase(repository: Repository): GetTodoUseCase {
        return GetTodoUseCaseImpl(repository)
    }


    // Tasks

    @Provides
    @Singleton
    fun providesTaskDao(context: Context): TaskDao = getDatabase(context).taskDao()

    @Provides
    @Singleton
    fun providesAddTaskUseCase(repository: Repository): AddTaskUseCase {
        return AddTaskUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesGetAllTasksUseCase(repository: Repository): GetAllTasksUseCase {
        return GetAllTasksUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesRemoveTaskUseCase(repository: Repository): RemoveTaskUseCase {
        return RemoveTaskUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesUpdateTaskUseCase(repository: Repository): UpdateTaskUseCase {
        return UpdateTaskUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesGetTaskUseCase(repository: Repository): GetTaskUseCase {
        return GetTaskUseCaseImpl(repository)
    }

    // Lists

    @Provides
    @Singleton
    fun providesListTodoDao(context: Context): ListTodoDao = getDatabase(context).listTodoDao()

    @Provides
    @Singleton
    fun providesAddListUseCase(repository: Repository): AddListUseCase {
        return AddListUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesGetAllListsUseCase(repository: Repository): GetAllListsUseCase {
        return GetAllListsUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesRemoveListUseCase(repository: Repository): RemoveListUseCase {
        return RemoveListUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesUpdateListUseCase(repository: Repository): UpdateListUseCase {
        return UpdateListUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun providesGetListUseCase(repository: Repository): GetListUseCase {
        return GetListUseCaseImpl(repository)
    }
}