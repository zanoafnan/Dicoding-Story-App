package com.dicoding.intermediate_submission.view.storydetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.intermediate_submission.databinding.ActivityStoryDetailBinding
import com.dicoding.intermediate_submission.di.ListStoryItem
import com.dicoding.intermediate_submission.di.Story

class StoryDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<ListStoryItem>("EXTRA_STORY")

        binding.tvUser.text = story?.name
        binding.tvDesc.text = story?.description

        Glide.with(this)
            .load(story?.photoUrl)
            .into(binding.ivPic)
    }
}
