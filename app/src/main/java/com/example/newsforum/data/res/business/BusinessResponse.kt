package com.example.newsforum.data.res.business

import com.google.gson.annotations.SerializedName

data class BusinessResponse(

	@field:SerializedName("totalResults")
	val totalResults: Int? = null,

	@field:SerializedName("articles")
	val articles: List<BusinessArticlesItem>? = null,

	@field:SerializedName("status")
	val status: String? = null
)