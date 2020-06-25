package com.nollpointer.dates.practise

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize

@Parcelize
class PractiseResult(
        var question: String,
        var isCorrect: Boolean) : Parcelable