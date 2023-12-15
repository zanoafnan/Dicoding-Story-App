package com.dicoding.intermediate_submission.view.storyadd

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StoryAddViewModel : ViewModel() {

    private val _isUploading = MutableLiveData<Boolean>()
    val isUploading: LiveData<Boolean>
        get() = _isUploading

    fun startUpload() {
        _isUploading.value = true
    }

    fun finishUpload() {
        _isUploading.value = false
    }
}
