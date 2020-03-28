package com.example.newsforum.data.res.search

import com.google.gson.annotations.SerializedName

data class SearchEverythingResponse(

    @field:SerializedName("totalResults")
	val totalResults: Int? = null,

    @field:SerializedName("articles")
	val articles: List<SearchArticlesItem>? = null,

    @field:SerializedName("status")
	val status: String? = null
)