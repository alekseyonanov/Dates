package com.nollpointer.dates.ui.details.terms

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.nollpointer.dates.api.WikipediaApi
import com.nollpointer.dates.model.Term
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.other.BaseViewModel
import com.nollpointer.dates.other.ExternalLinksManager
import com.nollpointer.dates.other.ReportManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * ViewModel экрана "Подробности по термину"
 *
 * @author Onanov Aleksey (@onanov)
 */
class TermsDetailsViewModel @ViewModelInject constructor(
        private val api: WikipediaApi,
        private val navigator: AppNavigator,
        private val externalLinksManager: ExternalLinksManager,
        private val reportManager: ReportManager,
) : BaseViewModel() {

    var term = Term()

    val wikiDescriptionLiveData = MutableLiveData<String>()
    val dataLiveData = MutableLiveData<Pair<String, String>>()
    val errorLiveData = MutableLiveData<String>()
    val loadingLiveData = MutableLiveData<Boolean>()

    private val specializedText: String
        get() = "${term.description[0].toLowerCase()}${term.description.substring(1)}"

    override fun onStart() {
        dataLiveData.value = term.term to specializedText
        load()
    }

    private fun load() {
        api.getData(term.request)
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
        externalLinksManager.navigateToWiki(term.request)
    }

    fun onGoogleLinkClicked() {
        externalLinksManager.navigateToGoogle(term.term)
    }

    fun onYandexLinkClicked() {
        externalLinksManager.navigateToYandex(term.term)
    }

    fun onArrowBackClicked() {
        navigator.navigateBack()
    }

    fun onReportClicked() {
        reportManager.reportTerm(term)
    }
}