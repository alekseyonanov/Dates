package com.nollpointer.dates.model

import com.google.gson.annotations.SerializedName

/**
 * Модель ответа сервера при запросе деталей по событию из Википедии
 *
 * @author Onanov Aleksey (@onanov)
 */
class WikipediaResponseModel {
    @SerializedName("title")
    var title: String? = null

    @SerializedName("displaytitle")
    var displaytitle: String? = null

    @SerializedName("pageid")
    var pageid: Int? = null

    @SerializedName("extract")
    var extract: String? = null

    @SerializedName("extract_html")
    var extractHtml: String? = null

    @SerializedName("lang")
    var lang: String? = null

    @SerializedName("dir")
    var dir: String? = null

    @SerializedName("timestamp")
    var timestamp: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("content_urls")
    var contentUrls: ContentUrls? = null

    class ContentUrls {
        @SerializedName("mobile")
        var mobile: Mobile? = null
    }

    class Mobile {
        @SerializedName("page")
        var page: String? = null
    }

}