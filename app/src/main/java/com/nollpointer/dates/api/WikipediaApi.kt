package com.nollpointer.dates.api

import com.nollpointer.dates.model.WikipediaResponseModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Api для работы с Википедией
 *
 * @author Onanov Aleksey (@onanov)
 */
interface WikipediaApi {
    @GET("page/summary/{title}")
    fun getData(@Path("title") title: String?): Single<WikipediaResponseModel>
}