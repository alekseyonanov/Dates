package com.nollpointer.dates.ui.view

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.nollpointer.dates.R
import com.nollpointer.dates.other.BottomNavigationViewDelegate
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @author Onanov Aleksey (@onanov)
 */
@AndroidEntryPoint
open class BaseFragment : Fragment() {

    @Inject
    lateinit var bottomNavigationViewDelegate: BottomNavigationViewDelegate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        * Немного ускорим работу и не будем каждый раз изменять цвет.
        */
        if (requireActivity().window.statusBarColor != ContextCompat.getColor(requireContext(), statusBarColorRes)) {

            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), statusBarColorRes)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                updateStatusBarIconsColor()
            }
        }

        if (isBottomNavigationViewHidden) {
            bottomNavigationViewDelegate.hide()
        } else {
            bottomNavigationViewDelegate.show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun updateStatusBarIconsColor() {
        requireActivity().window.decorView.systemUiVisibility =
                when (isStatusBarLight) {
                    true -> View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    else -> 0
                }
    }

    /*
    * Цвет статус бара
    */
    open val statusBarColorRes: Int = R.color.colorPrimary

    /*
    * Является ли статус бар светлым
    */
    open val isStatusBarLight: Boolean = false

    /*
    * Надо ли скрывать нижнее меню
    */
    open val isBottomNavigationViewHidden: Boolean = false
}