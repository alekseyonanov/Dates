package com.nollpointer.dates.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WikipediaResponseModel {
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("displaytitle")
    @Expose
    var displaytitle: String? = null
    @SerializedName("pageid")
    @Expose
    var pageid: Int? = null
    @SerializedName("extract")
    @Expose
    var extract: String? = null
    @SerializedName("extract_html")
    @Expose
    var extractHtml: String? = null
    @SerializedName("lang")
    @Expose
    var lang: String? = null
    @SerializedName("dir")
    @Expose
    var dir: String? = null
    @SerializedName("timestamp")
    @Expose
    var timestamp: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("content_urls")
    @Expose
    var contentUrls: ContentUrls? = null

}