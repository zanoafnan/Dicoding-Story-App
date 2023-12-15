package com.dicoding.intermediate_submission.view.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dicoding.intermediate_submission.data.StoryRepository
import com.dicoding.intermediate_submission.data.Result
import com.dicoding.intermediate_submission.di.ListStoryItem
import com.dicoding.intermediate_submission.di.Story
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStoriesWithLocation(): LiveData<Result<List<Story>>> = liveData {
        storyRepository.getStoriesWithLocation().collect { result ->
            when (result) {
                is Result.Success -> {
                    val stories = result.data.map { listStoryItemToStory(it) }
                     emit(Result.Success(stories))
                }
                is Result.Error -> {
                     emit(Result.Error(result.exception))
                }
            }
        }
    }

    // Convert ListStoryItem to Story
    private fun listStoryItemToStory(listStoryItem: ListStoryItem): Story {
        //val lon = listStoryItem.lon?.toDouble() ?: 0.0
        //val lat = listStoryItem.lat?.toDouble() ?: 0.0

        return Story(
            photoUrl = listStoryItem.photoUrl,
            createdAt = listStoryItem.createdAt,
            name = listStoryItem.name,
            description = listStoryItem.description,
            lon = listStoryItem.lon,
            id = listStoryItem.id,
            lat = listStoryItem.lat
        )
    }
}

