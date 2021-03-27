package com.nollpointer.dates.other

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.nollpointer.dates.BuildConfig
import com.nollpointer.dates.R
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.model.Term
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

/**
 * Менеджер для работы с жалобами.
 *
 * @author Onanov Aleksey (@onanov)
 */
class ReportManager @Inject constructor(@ActivityContext private val context: Context) {

    private val baseReportIntent: Intent
        get() = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(BuildConfig.MAIL_TO)
            putExtra(Intent.EXTRA_EMAIL, arrayOf(BuildConfig.EMAIL_REPORT))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.email_subject))
        }

    private val writableZone = "\n\n\n\n\n"

    private val baseAppInfo: String
        get() = context.getString(R.string.email_app_base_info,
                BuildConfig.VERSION_CODE,
                BuildConfig.VERSION_NAME)

    fun reportDate(date: Date) {
        navigateToMail(
                createDateReportIntent(date)
        )
    }

    fun reportTerm(term: Term) {
        navigateToMail(
                createTermReportIntent(term)
        )
    }

    fun reportPractise(practise: Practise) {
        navigateToMail(
                createPractiseReportIntent(practise)
        )
    }

    fun reportBug() {
        navigateToMail(
                createBugReportIntent()
        )
    }

    private fun createDateReportIntent(date: Date): Intent {
        val text = buildString {
            append(context.getString(R.string.email_report_date_text, date.dateFull, date.event))
            append(writableZone)
            append(baseAppInfo)
        }
        return baseReportIntent.putExtra(Intent.EXTRA_TEXT, text)
    }

    private fun createTermReportIntent(term: Term): Intent {
        val text = buildString {
            append(context.getString(R.string.email_report_term_text, term.term, term.description))
            append(writableZone)
            append(baseAppInfo)
        }
        return baseReportIntent.putExtra(Intent.EXTRA_TEXT, text)
    }

    private fun createPractiseReportIntent(practise: Practise): Intent {
        val title = context.getString(practise.practiseResId)
        val text = buildString {
            append(context.getString(R.string.email_report_practise_text, title))
            append(writableZone)
            append(baseAppInfo)
        }
        return baseReportIntent.putExtra(Intent.EXTRA_TEXT, text)
    }

    private fun createBugReportIntent(): Intent {
        val text = buildString {
            append(writableZone)
            append(baseAppInfo)
        }
        return baseReportIntent.putExtra(Intent.EXTRA_TEXT, text)
    }

    private fun navigateToMail(intent: Intent) {
        context.startActivity(intent)
    }

}