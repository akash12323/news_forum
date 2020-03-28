package com.example.newsforum.data.res.health

import com.google.gson.annotations.SerializedName

data class HealthResponse(

    @field:SerializedName("totalResults")
	val totalResults: Int? = null,

    @field:SerializedName("articles")
	val articles: List<HealthArticlesItem>? = null,

    @field:SerializedName("status")
	val status: String? = null
)