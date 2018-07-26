package com.nollpointer.dates;

import retrofit2.Call;
        import retrofit2.http.GET;
        import retrofit2.http.Path;

public interface WikipediaApi {
    @GET("/api/rest_v1/page/summary/{title}")
    Call<WikipediaResponseModel> getData(@Path("title") String title);
}
