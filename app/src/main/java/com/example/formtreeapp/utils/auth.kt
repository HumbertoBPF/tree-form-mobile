package com.example.formtreeapp.utils

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import com.example.formtreeapp.activities.MainActivity
import com.example.formtreeapp.data_store.Settings
import kotlinx.coroutines.flow.first
import java.util.Calendar

suspend fun getAuthorizationHeader(context: Context): String {
    val settings = Settings(context)
    return "Bearer ${settings.getToken().first()}"
}

suspend fun logout(context: Context) {
    val settings = Settings(context)
    settings.deleteToken()
    val intent = Intent(context, MainActivity::class.java)
    intent.addFlags(FLAG_ACTIVITY_CLEAR_TASK)
    context.startActivity(intent)
}

suspend fun checkSession(context: Context) {
    val settings = Settings(context)

    settings.getTokenExpiration().first { tokenExpiration ->
        val currentTime = Calendar.getInstance().timeInMillis

        if ((tokenExpiration != null) && (tokenExpiration <= currentTime)) {
            logout(context)
        }

        true
    }
}