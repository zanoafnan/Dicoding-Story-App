package com.dicoding.intermediate_submission.data

import android.util.Log
import com.dicoding.intermediate_submission.di.ApiService
import com.dicoding.intermediate_submission.di.RegisterResponse
import com.dicoding.intermediate_submission.data.pref.UserModel
import com.dicoding.intermediate_submission.data.pref.UserPreference
import com.dicoding.intermediate_submission.di.LoginResponse
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun registerUser(name: String, email: String, password: String): RegisterResponse {

        val response = apiService.register(name, email, password)

        if (response.error == true) {
            Log.e("Register", "Pendaftaran gagal: ${response.message}")
        } else {
            Log.d("Register", "Pendaftaran berhasil: ${response.message}")
        }

        return response
    }

    suspend fun loginUser(email: String, password: String): LoginResponse {
        // Call the login API endpoint using apiService.login
        val response = apiService.login(email, password)

        if (response.error) {
            Log.e("Login", "Login failed: ${response.message}")
        } else {
            Log.d("Login", "Login successful")
        }

        return response
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}
