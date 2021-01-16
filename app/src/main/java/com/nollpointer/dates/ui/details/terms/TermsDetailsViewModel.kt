package com.nollpointer.dates.ui.details.terms

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.nollpointer.dates.api.WikipediaApi
import com.nollpointer.dates.model.Term
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.other.BaseViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * @author Onanov Aleksey (@onanov)
 */
class TermsDetailsViewModel @ViewModelInject constructor(
        @ActivityContext private val context: Context,
        private val api: WikipediaApi,
        private val navigator: AppNavigator) : BaseViewModel() {

    var term = Term()

    val wikiDescriptionLiveData = MutableLiveData<String>()
    val dataLiveData = MutableLiveData<Pair<String, String>>()
    val errorLiveData = MutableLiveData<String>()
    val loadingLiveData = MutableLiveData<Boolean>()

    override fun onStart() {
        dataLiveData.value = term.term to specializedText()
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
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://ru.wikipedia.org/wiki/${term.term}"))
        context.startActivity(intent)
    }

    fun onGoogleLinkClicked() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=${getLink(term.term)}"))
        context.startActivity(intent)
    }

    fun onYandexLinkClicked() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/search/?text=${getLink(term.term)}"))
        context.startActivity(intent)
    }

    fun onArrowBackClicked() {
        navigator.navigateBack()
    }

    private fun getLink(originalText: String) = buildString {
        originalText.split(" ").forEach {
            append(it)
            append("+")
        }
    }

    private fun specializedText() = "${term.description[0].toLowerCase()}${term.description.substring(1)}"

}