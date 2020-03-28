package com.example.newsforum.data.res.politics

import com.google.gson.annotations.SerializedName

data class PoliticsResponse(

    @field:SerializedName("totalResults")
	val totalResults: Int? = null,

    @field:SerializedName("articles")
	val articles: List<PoliticsArticlesItem>? = null,

    @field:SerializedName("status")
	val status: String? = null
)