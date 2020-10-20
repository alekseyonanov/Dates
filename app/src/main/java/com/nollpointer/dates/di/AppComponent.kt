package com.nollpointer.dates.di

import android.app.Activity
import androidx.fragment.app.Fragment
import dagger.Component
import javax.inject.Singleton

/**
 * @author Onanov Aleksey (@onanov)
 */

@Singleton
@Component(
        modules = [
            AppModule::class
        ]
)
interface AppComponent {
    fun inject(activity: Activity)
    fun inject(fragment: Fragment)
}