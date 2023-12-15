package com.dicoding.intermediate_submission.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.intermediate_submission.data.pref.UserPreference
import com.dicoding.intermediate_submission.di.ApiService
import com.dicoding.intermediate_submission.di.ListStoryItem
import com.dicoding.intermediate_submission.di.StoryResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

    fun getStoriesWithLocation(): Flow<Result<List<ListStoryItem>>> = flow {
        val user = userPreference.getSession().firstOrNull()
        if (user != null && user.isLogin) {
            val token = user.token
            try {
                val response = apiService.getStoriesWithLocation()
                emit(Result.Success(response.listStory))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        } else {
            emit(Result.Error(Exception("User not logged in")))
        }
    }


    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(apiService: ApiService, userPreference: UserPreference): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, userPreference)
            }.also { instance = it }
    }
}

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

