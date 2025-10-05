package ir.noori.taskmanager.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.noori.taskmanager.core.constant.AppConstant
import ir.noori.taskmanager.domain.model.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = AppConstant.DATA_STORE_NAME)

class DataStore @Inject constructor(@ApplicationContext private val context: Context) {

    private val dataStore = context.dataStore
    companion object {
        val THEME_KEY = booleanPreferencesKey(AppConstant.IS_DARK_MODE)
        val ACCESS_TOKEN = stringPreferencesKey(AppConstant.ACCESS_TOKEN)
        val REFRESH_TOKEN = stringPreferencesKey(AppConstant.REFRESH_TOKEN)
        val USER_ID = stringPreferencesKey(AppConstant.USER_ID)
    }

    val isDarkMode: Flow<Boolean> = dataStore.data
        .map { it[THEME_KEY] ?: false }

    suspend fun toggleTheme(current: Boolean) {
        dataStore.edit { it[THEME_KEY] = !current }
    }

    suspend fun saveSession(session: UserSession){
        dataStore.edit { prefs->
            prefs[ACCESS_TOKEN] = session.accessToken
            prefs[REFRESH_TOKEN] = session.refreshToken
            prefs[USER_ID] = "1"
        }
    }

    fun observeSession(): Flow<UserSession?> =
        dataStore.data.map {  prefs->
            val access = prefs[ACCESS_TOKEN] ?: return@map null
            if(access.isBlank()) return@map null
            UserSession(
                accessToken = access,
                refreshToken = prefs[REFRESH_TOKEN] ?: ""
            )
        }


    val accessToken: Flow<String> = dataStore.data.map { it[ACCESS_TOKEN]?: ""}

    suspend fun setAccessToken(token: String) {
        dataStore.edit { it[ACCESS_TOKEN] = token }
    }

    val refreshToken: Flow<String> = dataStore.data.map { it[REFRESH_TOKEN]?: ""}

    suspend fun setRefreshToken(token: String) {
        dataStore.edit { it[REFRESH_TOKEN] = token }
    }
}
