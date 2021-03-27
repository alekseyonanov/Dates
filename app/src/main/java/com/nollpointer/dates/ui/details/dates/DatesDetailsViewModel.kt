package com.nollpointer.dates.ui.details.dates

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.nollpointer.dates.api.WikipediaApi
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.other.BaseViewModel
import com.nollpointer.dates.other.ExternalLinksManager
import com.nollpointer.dates.other.ReportManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * ViewModel экрана "Подробности по дате"
 *
 * @author Onanov Aleksey (@onanov)
 */
class DatesDetailsViewModel @ViewModelInject constructor(
        private val api: WikipediaApi,
        private val navigator: AppNavigator,
        private val externalLinksManager: ExternalLinksManager,
        private val reportManager: ReportManager,
) : BaseViewModel() {

    var date = Date()

    val wikiDescriptionLiveData = MutableLiveData<String>()
    val dataLiveData = MutableLiveData<Pair<String, String>>()
    val errorLiveData = MutableLiveData<String>()
    val loadingLiveData = MutableLiveData<Boolean>()

    private val specializedText: String
        get() = "${date.event[0].toLowerCase()}${date.event.substring(1)}"

    override fun onStart() {
        dataLiveData.value = date.date to specializedText
        load()
    }

    private fun load() {
        api.getData(date.request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadingLiveData.value = true }
                .doAfterTerminate { loadingLiveData.value = false }
                .subscribe({
                    wikiDescriptionLiveData.value = it.extractHtml
                }, {
                    errorLiveData.value = it.message
                    Timber.e(it)
                }).disposeOnCleared()
    }

    fun onWikiLinkClicked() {
        externalLinksManager.navigateToWiki(date.request)
    }

    fun onGoogleLinkClicked() {
        externalLinksManager.navigateToGoogle(date.event)
    }

    fun onYandexLinkClicked() {
        externalLinksManager.navigateToYandex(date.event)
    }

    fun onArrowBackClicked() {
        navigator.navigateBack()
    }

    fun onReportClicked() {
        reportManager.reportDate(date)
    }
}