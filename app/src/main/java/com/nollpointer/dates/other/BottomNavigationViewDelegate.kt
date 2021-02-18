package com.nollpointer.dates.other

import android.content.Context
import com.nollpointer.dates.ui.activity.MainActivity
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

/**
 * @author Onanov Aleksey (@onanov)
 */
class BottomNavigationViewDelegate @Inject constructor(@ActivityContext private val context: Context) {

    private val activity: MainActivity
        get() = context as MainActivity

    fun show() {
        activity.showBottomNavigationView()
    }

    fun hide() {
        activity.hideBottomNavigationView()
    }

}