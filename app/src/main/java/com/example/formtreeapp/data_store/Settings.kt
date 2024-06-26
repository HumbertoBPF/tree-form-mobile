package com.example.formtreeapp.data_store

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.formtreeapp.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar

class Settings(private val context: Context) {
    companion object {
        private val Context.dataStore by preferencesDataStore(name = BuildConfig.NAME_PREFERENCES_DATA_STORE)
        private const val TOKEN_KEY = "token"
        private const val TOKEN_EXPIRATION_KEY = "token_expiration"
    }

    fun getToken(): Flow<String?> {
        return context.dataStore.data.map { settings ->
            val tokenPreferenceKey = stringPreferencesKey(TOKEN_KEY)
            val username = settings[tokenPreferenceKey]
            username
        }
    }

    fun getTokenExpiration(): Flow<Long?> {
        return context.dataStore.data.map { preferences ->
            val tokenExpirationPreferencesKey = longPreferencesKey(TOKEN_EXPIRATION_KEY)
            val tokenExpiration = preferences[tokenExpirationPreferencesKey]
            tokenExpiration
        }
    }

    suspend fun setToken(token: String) {
        val tokenPreferencesKey = stringPreferencesKey(TOKEN_KEY)
        val tokenExpirationPreferencesKey = longPreferencesKey(TOKEN_EXPIRATION_KEY)

        context.dataStore.edit { settings ->
            settings[tokenPreferencesKey] = token
            settings[tokenExpirationPreferencesKey] = Calendar.getInstance().timeInMillis + 3500 * 1000
        }
    }

    suspend fun deleteToken() {
        val tokenPreferencesKey = stringPreferencesKey(TOKEN_KEY)
        val tokenExpirationPreferencesKey = longPreferencesKey(TOKEN_EXPIRATION_KEY)

        context.dataStore.edit { settings ->
            settings.remove(tokenPreferencesKey)
            settings.remove(tokenExpirationPreferencesKey)
        }
    }
}
