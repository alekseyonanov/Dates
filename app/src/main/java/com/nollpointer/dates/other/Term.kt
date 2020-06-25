package com.nollpointer.dates.other

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Term(var term: String, var description: String): Parcelable