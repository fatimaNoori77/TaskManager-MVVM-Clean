package ir.noori.taskmanager.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "settings")

class DataStore @Inject constructor(@ApplicationContext private val context: Context) {
    companion object {
        val THEME_KEY = booleanPreferencesKey("is_dark_mode")
    }

    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { it[THEME_KEY] ?: false }

    suspend fun toggleTheme(current: Boolean) {
        context.dataStore.edit { it[THEME_KEY] = !current }
    }
}
