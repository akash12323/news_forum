package com.example.newsforum.data.res

import com.google.gson.annotations.SerializedName

data class TopNewsResponse(

	@field:SerializedName("totalResults")
	val totalResults: Int? = null,

	@field:SerializedName("articles")
	val articles: List<TopNewsArticlesItem>? = null,

	@field:SerializedName("status")
	val status: String? = null
)