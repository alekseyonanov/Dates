package com.nollpointer.dates.ui.menu

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nollpointer.dates.annotation.FULL
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.other.BaseViewModel
import com.nollpointer.dates.other.ExternalLinksManager
import com.nollpointer.dates.ui.activity.MainActivity
import dagger.hilt.android.qualifiers.ActivityContext

/**
 * ViewModel экрана "Меню"
 *
 * @author Onanov Aleksey (@onanov)
 */
class MenuViewModel @ViewModelInject constructor(
        @ActivityContext private val context: Context,
        private val navigator: AppNavigator,
        private val externalLinksManager: ExternalLinksManager,
) : BaseViewModel() {

    private val activity: MainActivity
        get() = context as MainActivity


    private val _modeLiveData = MutableLiveData<Int>()
    val modeLiveData: LiveData<Int> = _modeLiveData

    private val _modeSelectVisibilityLiveData = MutableLiveData<Boolean>()
    val modeSelectVisibilityLiveData: LiveData<Boolean> = _modeSelectVisibilityLiveData

    private var mode = FULL

    override fun onStart() {
        mode = activity.mode
        _modeLiveData.value = mode
    }

    fun onTelegramClicked() {
        externalLinksManager.navigateToTelegram()
    }

    fun onTwitterClicked() {
        externalLinksManager.navigateToTwitter()
    }

    fun onMailClicked() {
        externalLinksManager.navigateToMail()
    }

    fun onVkClicked() {
        externalLinksManager.navigateToVk()
    }

    fun onInstagramClicked() {
        externalLinksManager.navigateToInstagram()
    }

    fun onOnanovClicked() {
        externalLinksManager.navigateToOnanov()
    }

    fun onGameClicked() {
        navigator.navigateToGame()
    }

    fun onSettingsClicked() {
        navigator.navigateToSettings()
    }

    fun onPageSelected(item: Int) {
        _modeSelectVisibilityLiveData.value = mode != item
    }

    fun onModeSelectClicked(currentMode: Int) {
        mode = currentMode
        activity.mode = currentMode
        _modeSelectVisibilityLiveData.value = false
    }

}