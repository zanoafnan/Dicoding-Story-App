package com.dicoding.intermediate_submission.view.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.intermediate_submission.data.StoryRepository
import com.dicoding.intermediate_submission.data.Result
import com.dicoding.intermediate_submission.di.ListStoryItem
import com.dicoding.intermediate_submission.di.Story
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    val story: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStories().cachedIn(viewModelScope)

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
