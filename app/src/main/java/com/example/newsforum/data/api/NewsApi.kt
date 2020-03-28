package com.example.newsforum.data.api

import com.example.newsforum.data.res.TopNewsResponse
import com.example.newsforum.data.res.business.BusinessResponse
import com.example.newsforum.data.res.entertainment.EntertainmentResponse
import com.example.newsforum.data.res.health.HealthResponse
import com.example.newsforum.data.res.science.ScienceResponse
import com.example.newsforum.data.res.search.SearchEverythingResponse
import com.example.newsforum.data.res.sports.SportsResponse
import com.example.newsforum.data.res.technology.TechnologyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("top-headlines?apiKey=1c0fedf4d3414f9389a056a833ce10d2")
    suspend fun getTopNews(@Query("country")country:String):Response<TopNewsResponse>

    @GET("top-headlines?apiKey=1c0fedf4d3414f9389a056a833ce10d2")
    suspend fun getSportsNews(@Query("country")country:String,
                              @Query("category")category:String):Response<SportsResponse>

    @GET("top-headlines?apiKey=1c0fedf4d3414f9389a056a833ce10d2")
    suspend fun getScienceNews(@Query("country")country:String,
                              @Query("category")category:String):Response<ScienceResponse>

    @GET("top-headlines?apiKey=1c0fedf4d3414f9389a056a833ce10d2")
    suspend fun getHealthNews(@Query("country")country:String,
                              @Query("category")category:String):Response<HealthResponse>

    @GET("top-headlines?apiKey=1c0fedf4d3414f9389a056a833ce10d2")
    suspend fun getEntertainmentNews(@Query("country")country:String,
                              @Query("category")category:String):Response<EntertainmentResponse>

    @GET("top-headlines?apiKey=1c0fedf4d3414f9389a056a833ce10d2")
    suspend fun getBusinessNews(@Query("country")country:String,
                              @Query("category")category:String):Response<BusinessResponse>

    @GET("top-headlines?apiKey=1c0fedf4d3414f9389a056a833ce10d2")
    suspend fun getTechnologyNews(@Query("country")country:String,
                              @Query("category")category:String):Response<TechnologyResponse>

    @GET("everything?sortBy=popularity&apiKey=1c0fedf4d3414f9389a056a833ce10d2")
    suspend fun searchNews(@Query("q")q:String,
                           @Query("sortBy")sortBy:String,
                           @Query("apiKey")apiKey:String):Response<SearchEverythingResponse>
}

//https://newsapi.org/v2/top-headlines?country=us&apiKey=1c0fedf4d3414f9389a056a833ce10d2

// @GET("everything?q=apple&from=2020-03-26&to=2020-03-26&sortBy=popularity&apiKey=1c0fedf4d3414f9389a056a833ce10d2")