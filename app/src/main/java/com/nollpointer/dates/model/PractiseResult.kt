package com.nollpointer.dates.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class PractiseResult(
        var questionDate: Date,
        var answerDate: Date,
        var isCorrect: Boolean) : Parcelable