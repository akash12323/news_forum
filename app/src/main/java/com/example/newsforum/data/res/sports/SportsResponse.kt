package com.example.newsforum.data.res.sports

import com.google.gson.annotations.SerializedName

data class SportsResponse(

    @field:SerializedName("totalResults")
	val totalResults: Int? = null,

    @field:SerializedName("articles")
	val articles: List<SportsArticlesItem>? = null,

    @field:SerializedName("status")
	val status: String? = null
)