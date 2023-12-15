package com.dicoding.intermediate_submission.view.story

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.intermediate_submission.R
import com.dicoding.intermediate_submission.databinding.ActivityStoryBinding
import com.dicoding.intermediate_submission.di.Injection
import kotlinx.coroutines.launch
import com.dicoding.intermediate_submission.data.Result
import com.dicoding.intermediate_submission.view.map.MapsActivity
import com.dicoding.intermediate_submission.view.storyadd.StoryAddActivity

class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding
    private val storyViewModel: StoryViewModel by viewModels {
        StoryViewModelFactory(Injection.provideStoryRepository(this))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyRepository = Injection.provideStoryRepository(this)
        val layoutManager = LinearLayoutManager(this)
        val storyAdapter = StoryAdapter()
        binding.rvStory.layoutManager = layoutManager
        binding.rvStory.adapter = storyAdapter

         val dividerItemDecoration = DividerItemDecoration(binding.rvStory.context, layoutManager.orientation)
        binding.rvStory.addItemDecoration(dividerItemDecoration)

        binding.fabAdd.setOnClickListener {
            showLoading(true)
            val intent = Intent(this, StoryAddActivity::class.java)
            startActivity(intent)
        }

        getData()

    }

    private fun getData() {
        val adapter = StoryAdapter()
        binding.rvStory.adapter = adapter
        storyViewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.map -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
