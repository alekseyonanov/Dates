package com.nollpointer.dates.model

import android.os.Parcelable
import com.nollpointer.dates.annotation.Mode
import com.nollpointer.dates.annotation.Practise
import com.nollpointer.dates.annotation.Type
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
) : Parcelable