package com.nollpointer.dates.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nollpointer.dates.api.WikipediaApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * @author Onanov Aleksey (@onanov)
 */
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideRetroFit(gson: Gson): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://ru.wikipedia.org")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().setLenient().create()
    }

    @Singleton
    @Provides
    fun provideWikipediaApi(retrofit: Retrofit): WikipediaApi{
        return retrofit.create(WikipediaApi::class.java)
    }


}