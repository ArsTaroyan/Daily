package am.a_t.dailyapp.data.source.dao

import am.a_t.dailyapp.domain.module.Todo
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTodo(todo: Todo)

    @Delete
    suspend fun removeTodo(todo: Todo)

    @Update
    suspend fun updateTodo(todo: Todo)

    @Query("SELECT * FROM todos WHERE list_id = :id")
    fun getAllTodos(id: Long): Flow<List<Todo>>

    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getTodo(id: Long): Todo?
}