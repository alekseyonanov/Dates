package com.nollpointer.dates.model

import android.os.Parcelable
import androidx.annotation.StringRes
import com.nollpointer.dates.R
import com.nollpointer.dates.annotation.*
import com.nollpointer.dates.annotation.Practise
import kotlinx.parcelize.Parcelize

/**
 * Модель практики
 *
 * @author Onanov Aleksey (@onanov)
 */
@Parcelize
data class Practise(
        @Practise var practise: Int = 0,
        @Mode var mode: Int = 0,
        @Type var type: Int = 0,
        var dates: List<Date> = emptyList(),
        var isTestMode: Boolean = false,
        var results: List<PractiseResult> = emptyList(),
) : Parcelable {

    val practiseResId: Int
        @StringRes
        get() = when (practise) {
            CARDS -> R.string.fragment_dates_practise_cards_title
            VOICE -> R.string.fragment_dates_practise_voice_title
            TEST -> R.string.fragment_dates_practise_test_title
            TRUE_FALSE -> R.string.fragment_dates_practise_true_false_title
            LINK -> R.string.fragment_dates_practise_link_title
            SORT -> R.string.fragment_dates_practise_sort_title
            DISTRIBUTION -> R.string.fragment_dates_practise_distribution_title
            else -> R.string.fragment_dates_practise_cards_title
        }
}