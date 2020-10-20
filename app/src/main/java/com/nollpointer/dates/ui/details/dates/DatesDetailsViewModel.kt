package com.nollpointer.dates.ui.details.dates

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.nollpointer.dates.api.WikipediaApi
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.other.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * @author Onanov Aleksey (@onanov)
 */

class DatesDetailsViewModel(private val context: Context, private val api: WikipediaApi) : BaseViewModel() {

    var date = Date()

    private var loadDisposable = Disposables.disposed()

    val wikiDescriptionLiveData = MutableLiveData<String>()
    val dataLiveData = MutableLiveData<Pair<String, String>>()
    val errorLiveData = MutableLiveData<String>()
    val loadingLiveData = MutableLiveData<Boolean>()

    override fun onStart() {
        dataLiveData.value = date.date to specializedText()
        load()
    }

    override fun onCleared() {
        super.onCleared()
        loadDisposable.dispose()
    }

    private fun load() {
        loadDisposable = api.getData(date.request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { loadingLiveData.value = true }
                .doAfterTerminate { loadingLiveData.value = false }
                .subscribe({
                    wikiDescriptionLiveData.value = it.extractHtml
                }, {
                    errorLiveData.value = it.message
                    Timber.e(it)
                })
    }

    fun onWikiLinkClicked() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://ru.wikipedia.org/wiki/${date.request}"))
        context.startActivity(intent)
    }

    fun onGoogleLinkClicked() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=${getLink(date.event)}"))
        context.startActivity(intent)
    }

    fun onYandexLinkClicked() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/search/?text=${getLink(date.event)}"))
        context.startActivity(intent)
    }

    private fun getLink(originalText: String) = buildString {
        originalText.split(" ").forEach {
            append(it)
            append("+")
        }
    }

    private fun specializedText() = "${date.event[0].toLowerCase()}${date.event.substring(1)}"

}