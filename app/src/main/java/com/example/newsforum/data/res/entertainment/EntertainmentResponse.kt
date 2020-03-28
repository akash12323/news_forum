package com.example.newsforum.data.res.entertainment

import com.google.gson.annotations.SerializedName

data class EntertainmentResponse(

    @field:SerializedName("totalResults")
	val totalResults: Int? = null,

    @field:SerializedName("articles")
	val articles: List<EntertainmentArticlesItem>? = null,

    @field:SerializedName("status")
	val status: String? = null
)