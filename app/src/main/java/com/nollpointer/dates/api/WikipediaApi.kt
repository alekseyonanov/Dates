package com.nollpointer.dates.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WikipediaApi {
    @GET("/api/rest_v1/page/summary/{title}")
    fun getData(@Path("title") title: String?): Call<WikipediaResponseModel?>?
}