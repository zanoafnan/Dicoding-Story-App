package com.dicoding.intermediate_submission.view.storydetail

import androidx.lifecycle.ViewModel
import com.dicoding.intermediate_submission.di.Story

class StoryDetailViewModel : ViewModel() {
    private var story: Story? = null

    fun setStory(story: Story) {
        this.story = story
    }

    fun getStory(): Story? {
        return story
    }
}
