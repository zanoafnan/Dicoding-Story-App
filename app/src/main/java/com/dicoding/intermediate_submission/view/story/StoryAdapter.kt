package com.dicoding.intermediate_submission.view.story

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.intermediate_submission.R
import com.dicoding.intermediate_submission.di.ListStoryItem
import com.dicoding.intermediate_submission.di.Story
import com.dicoding.intermediate_submission.view.storydetail.StoryDetailActivity
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil


class StoryAdapter() :
    PagingDataAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {


    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUser: TextView = itemView.findViewById(R.id.tvUser)
        val tvDesc: TextView = itemView.findViewById(R.id.tvDesc)
        val ivPic: ImageView = itemView.findViewById(R.id.ivPic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }

    }

    class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var ivPic: ImageView = itemView.findViewById(R.id.ivPic)
        private var tvName: TextView = itemView.findViewById(R.id.tvUser)
        private var tvDescription: TextView = itemView.findViewById(R.id.tvDesc)

        fun bind(story: ListStoryItem) {
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(ivPic)
            tvName.text = story.name
            tvDescription.text = story.description

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, StoryDetailActivity::class.java)
                intent.putExtra("EXTRA_STORY", story as Parcelable)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(ivPic, "pic"),
                        Pair(tvName, "name"),
                        Pair(tvDescription, "desc")
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
