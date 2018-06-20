package com.nollpointer.dates;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {
    private static WikipediaApi wiki;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://ru.wikipedia.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        wiki = retrofit.create(WikipediaApi.class);
    }

    public static WikipediaApi getApi(){
        return wiki;
    }
}
