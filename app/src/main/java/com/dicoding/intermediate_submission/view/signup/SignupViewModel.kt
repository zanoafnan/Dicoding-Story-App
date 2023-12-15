package com.dicoding.intermediate_submission.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.intermediate_submission.di.RegisterResponse
import com.dicoding.intermediate_submission.data.UserRepository
import kotlinx.coroutines.launch

class SignupViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun registerUser(name: String, email: String, password: String, onResult: (RegisterResponse) -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = userRepository.registerUser(name, email, password)
                onResult(response)
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }
}
