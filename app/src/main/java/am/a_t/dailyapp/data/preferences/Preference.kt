package am.a_t.dailyapp.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.core.remove
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.first

class Preference(context: Context) {

    private val dataStore = context.createDataStore("DataStoreType")

    suspend fun saveType(key: String, value: String) {
        dataStore.edit {
            it[preferencesKey<String>(key)] = value
        }
    }

    suspend fun readType(key: String): String? = dataStore.data.first()[preferencesKey(key)]

    suspend fun removeType(key: String) {
        dataStore.edit {
            it.remove(preferencesKey<String>(key))
        }
    }

    companion object {
        const val TYPE = "type"
        const val TASK_ID = "task_id"
        const val TASK = "task"
    }

}