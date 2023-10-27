package am.a_t.dailyapp.data.source.dao

import am.a_t.dailyapp.domain.module.ListTodo
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ListTodoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addList(listTodo: ListTodo)

    @Delete
    suspend fun removeList(listTodo: ListTodo)

    @Update
    suspend fun updateList(listTodo: ListTodo)

    @Query("SELECT * FROM lists")
    fun getAllLists(): Flow<List<ListTodo>>

    @Query("SELECT * FROM lists WHERE id = :id")
    suspend fun getList(id: Long): ListTodo?
}