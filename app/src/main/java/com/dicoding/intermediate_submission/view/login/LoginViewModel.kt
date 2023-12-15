package com.dicoding.intermediate_submission.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.intermediate_submission.data.UserRepository
import com.dicoding.intermediate_submission.data.pref.UserModel
import com.dicoding.intermediate_submission.di.LoginResponse
import kotlinx.coroutines.launch


class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    suspend fun login(email: String, password: String): LoginResponse {
        _isLoading.value = true
        // Call the login API
        val response = repository.loginUser(email, password)

        if (!response.error) {
            _isLoading.value = false
            // Login successful, save the user's session and token to DataStore
            val userModel = UserModel(email, response.loginResult.token)
            repository.saveSession(userModel)
        } else {
            _isLoading.value = false
        }

        return response
    }
}