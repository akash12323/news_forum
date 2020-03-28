package com.example.newsforum.data.res.science

import com.google.gson.annotations.SerializedName

data class ScienceResponse(

    @field:SerializedName("totalResults")
	val totalResults: Int? = null,

    @field:SerializedName("articles")
	val articles: List<ScienceArticlesItem>? = null,

    @field:SerializedName("status")
	val status: String? = null
)