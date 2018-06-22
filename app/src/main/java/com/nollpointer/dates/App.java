package com.nollpointer.dates;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends MultiDexApplication {
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
