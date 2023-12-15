package com.dicoding.intermediate_submission.di

import android.content.Context
import com.dicoding.intermediate_submission.data.StoryRepository
import com.dicoding.intermediate_submission.data.UserRepository
import com.dicoding.intermediate_submission.data.pref.UserPreference
import com.dicoding.intermediate_submission.data.pref.dataStore
import com.dicoding.intermediate_submission.view.story.StoryViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = provideApiService(context, pref)
        return UserRepository.getInstance(pref, apiService)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return StoryRepository.getInstance(provideApiService(context, pref), pref)
    }

    fun provideStoryRepositoryWithLocation(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return StoryRepository.getInstance(provideApiService(context, pref), pref)
    }

    fun provideStoryViewModel(context: Context): StoryViewModel {
        val storyRepository = provideStoryRepository(context)
        return StoryViewModel(storyRepository)
    }

    fun provideApiService(context: Context, pref: UserPreference): ApiService {
        val user = runBlocking { pref.getUser().first() }
        val token = user?.token
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(requestHeaders)
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }



}



