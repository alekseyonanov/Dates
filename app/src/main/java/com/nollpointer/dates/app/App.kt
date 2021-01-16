package com.nollpointer.dates.app

import android.app.Application
import com.flurry.android.FlurryAgent
import com.google.firebase.analytics.FirebaseAnalytics
import com.nollpointer.dates.BuildConfig
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import dagger.hilt.android.HiltAndroidApp


/**
 * @author Onanov Aleksey (@onanov)
 */
@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        FlurryAgent.Builder()
                .withLogEnabled(true)
                .withCaptureUncaughtExceptions(true)
                .build(this, BuildConfig.FLURRY_KEY)
        val config = YandexMetricaConfig.newConfigBuilder(BuildConfig.APP_METRICA_KEY).build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.enableActivityAutoTracking(this)
        FirebaseAnalytics.getInstance(this)
    }
}