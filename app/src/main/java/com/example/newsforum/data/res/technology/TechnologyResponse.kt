package com.example.newsforum.data.res.technology

import com.google.gson.annotations.SerializedName

data class TechnologyResponse(

    @field:SerializedName("totalResults")
	val totalResults: Int? = null,

    @field:SerializedName("articles")
	val articles: List<TechnologyArticlesItem>? = null,

    @field:SerializedName("status")
	val status: String? = null
)