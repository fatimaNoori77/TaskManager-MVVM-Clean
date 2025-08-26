package ir.noori.taskmanager.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.noori.taskmanager.core.constant.AppConstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = AppConstant.DATA_STORE_NAME)

class DataStore @Inject constructor(@ApplicationContext private val context: Context) {
    companion object {
        val THEME_KEY = booleanPreferencesKey(AppConstant.IS_DARK_MODE)
        val ACCESS_TOKEN = stringPreferencesKey(AppConstant.ACCESS_TOKEN)
        val REFRESH_TOKEN = stringPreferencesKey(AppConstant.REFRESH_TOKEN)
    }

    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { it[THEME_KEY] ?: false }

    suspend fun toggleTheme(current: Boolean) {
        context.dataStore.edit { it[THEME_KEY] = !current }
    }

    val accessToken: Flow<String> = context.dataStore.data.map { it[ACCESS_TOKEN]?: ""}

    suspend fun setAccessToken(token: String) {
        context.dataStore.edit { it[ACCESS_TOKEN] = token }
    }

    val refreshToken: Flow<String> = context.dataStore.data.map { it[REFRESH_TOKEN]?: ""}

    suspend fun setRefreshToken(token: String) {
        context.dataStore.edit { it[REFRESH_TOKEN] = token }
    }
}
