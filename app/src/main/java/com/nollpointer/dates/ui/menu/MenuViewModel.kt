package com.nollpointer.dates.ui.menu

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.nollpointer.dates.R
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.other.BaseViewModel
import com.nollpointer.dates.ui.activity.MainActivity
import dagger.hilt.android.qualifiers.ActivityContext

/**
 * ViewModel экрана "Меню"
 *
 * @author Onanov Aleksey (@onanov)
 */
class MenuViewModel @ViewModelInject constructor(
        @ActivityContext private val context: Context,
        private val navigator: AppNavigator) : BaseViewModel() {

    private val activity = context as MainActivity

    val modeLiveData = MutableLiveData<Int>()
    val modeSelectVisibilityLiveData = MutableLiveData<Boolean>()

    private var mode = MainActivity.FULL_DATES_MODE

    override fun onStart() {
        mode = activity.mode
        modeLiveData.value = mode
    }

    fun onTelegramClicked() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://t.me/alekseyonanov"))
        context.startActivity(intent)
    }

    fun onTwitterClicked() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/alekseyonanov"))
        context.startActivity(intent)
    }

    fun onMailClicked() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("dates@onanov.ru"))
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.message_developer_title))
        context.startActivity(intent)
    }

    fun onVkClicked() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/onanov"))
        context.startActivity(intent)
    }

    fun onInstagramClicked() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/alekseyonanov"))
        context.startActivity(intent)
    }

    fun onOnanovClicked() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://onanov.ru/"))
        context.startActivity(intent)
    }

    fun onGameClicked() {
        navigator.navigateToGame()
    }

    fun onSettingsClicked() {
        navigator.navigateToSettings()
    }

    fun onPageSelected(item: Int) {
        modeSelectVisibilityLiveData.value = mode != item
    }

    fun onModeSelectClicked(currentMode: Int) {
        mode = currentMode
        activity.mode = currentMode
        modeSelectVisibilityLiveData.value = false
    }

}