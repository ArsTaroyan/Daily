package am.a_t.dailyapp.di

import am.a_t.dailyapp.data.repo.RepositoryImpl
import am.a_t.dailyapp.data.source.database.ListDatabase
import am.a_t.dailyapp.domain.iteractors.TaskDao
import am.a_t.dailyapp.domain.iteractors.TodoDao
import am.a_t.dailyapp.domain.repo.Repository
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
        RepositoryImpl(getDatabase(context).todoDao(), getDatabase(context).taskDao())

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

    @Provides
    @Singleton
    fun providesTodoDao(context: Context): TodoDao = getDatabase(context).todoDao()

    @Provides
    @Singleton
    fun providesTaskDao(context: Context): TaskDao = getDatabase(context).taskDao()

}