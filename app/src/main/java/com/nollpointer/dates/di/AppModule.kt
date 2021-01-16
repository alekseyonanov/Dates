package com.nollpointer.dates.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nollpointer.dates.BuildConfig
import com.nollpointer.dates.api.WikipediaApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ActivityContext
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Onanov Aleksey (@onanov)
 */
@Module
@InstallIn(ActivityComponent::class, FragmentComponent::class, ActivityRetainedComponent::class)
object AppModule {

    @Provides
    fun gson(): Gson {
        return GsonBuilder().setLenient().create()
    }

    @Provides
    fun retroFit(gson: Gson): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.WIKIPEDIA_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    fun wikipediaApi(retrofit: Retrofit): WikipediaApi {
        return retrofit.create(WikipediaApi::class.java)
    }

    //TODO: Решить, надо ли

    @Provides
    fun firebaseAnalytics(@ActivityContext context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }

}