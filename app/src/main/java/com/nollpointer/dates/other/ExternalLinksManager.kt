package com.nollpointer.dates.other

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.nollpointer.dates.BuildConfig
import com.nollpointer.dates.R
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

/**
 * Менеджер для работы с внешними ссылками
 *
 * @author Onanov Aleksey (@onanov)
 */
class ExternalLinksManager @Inject constructor(@ActivityContext private val context: Context) {

    fun navigateToTelegram() {
        navigateToExternalBrowser(BuildConfig.TELEGRAM)
    }

    fun navigateToTwitter() {
        navigateToExternalBrowser(BuildConfig.TWITTER)
    }

    fun navigateToMail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(BuildConfig.MAIL_TO)
            putExtra(Intent.EXTRA_EMAIL, arrayOf(BuildConfig.EMAIL_CONTACT))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.email_subject))
        }
        context.startActivity(intent)
    }

    fun navigateToVk() {
        navigateToExternalBrowser(BuildConfig.VK)
    }

    fun navigateToInstagram() {
        navigateToExternalBrowser(BuildConfig.INSTAGRAM)
    }

    fun navigateToOnanov() {
        navigateToExternalBrowser(BuildConfig.ONANOV_RU)
    }

    fun navigateToRateApp() {
        navigateToExternalBrowser(BuildConfig.PLAY_MARKET)
    }

    fun navigateToPrivacyPolicy() {
        navigateToExternalBrowser(BuildConfig.PRIVACY_POLICY)
    }

    fun navigateToTermsConditions() {
        navigateToExternalBrowser(BuildConfig.TERMS_CONDITIONS)
    }

    fun navigateToLicence() {
        navigateToExternalBrowser(BuildConfig.LICENCE)
    }

    fun navigateToFaq() {
        navigateToExternalBrowser(BuildConfig.FAQ)
    }

    fun navigateToBetaTest() {
        navigateToExternalBrowser(BuildConfig.BETA_TEST)
    }

    fun navigateToYandex(request: String) {
        navigateToSearch(BuildConfig.SEARCH_YANDEX, createRequestString(request))
    }

    fun navigateToGoogle(request: String) {
        navigateToSearch(BuildConfig.SEARCH_GOOGLE, createRequestString(request))
    }

    fun navigateToWiki(request: String) {
        navigateToSearch(BuildConfig.SEARCH_WIKIPEDIA, createRequestString(request))
    }

    private fun createRequestString(originalText: String) = buildString {
        originalText.split(" ").forEach {
            append(it)
            append("+")
        }
    }

    private fun navigateToSearch(base: String, request: String) {
        navigateToExternalBrowser("$base$request")
    }

    private fun navigateToExternalBrowser(link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        context.startActivity(intent)
    }

}