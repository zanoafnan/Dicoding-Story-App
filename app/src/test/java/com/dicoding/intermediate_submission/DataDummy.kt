package com.dicoding.intermediate_submission

import com.dicoding.intermediate_submission.di.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val quote = ListStoryItem(
                "https://example.com/photo.jpg",
                "2023-10-28",
                "Sample Name",
                "This is a sample description.",
                123.456,
                "sampleId",
                78.910
            )
            items.add(quote)
        }
        return items
    }
}