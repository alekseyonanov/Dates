package com.nollpointer.dates.ui.view

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * @author Onanov Aleksey (@onanov)
 */
abstract class BaseFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        * Немного ускорим работу и не будем каждый раз изменять цвет.
        */
        if (requireActivity().window.statusBarColor != ContextCompat.getColor(context as Context, getStatusBarColorRes())) {

            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            requireActivity().window.statusBarColor = ContextCompat.getColor(context as Context, getStatusBarColorRes())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                updateStatusBarIconsColor()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun updateStatusBarIconsColor() {
        requireActivity().window.decorView.systemUiVisibility =
                when (isStatusBarLight()) {
                    true -> View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    else -> 0
                }
    }

    abstract fun getStatusBarColorRes(): Int

    abstract fun isStatusBarLight(): Boolean
}