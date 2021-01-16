package com.nollpointer.dates.other

import android.text.Editable
import android.text.TextWatcher

/**
 * @author Onanov Aleksey (@onanov)
 */
open class SimpleTextWatcher : TextWatcher {

    override fun beforeTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        /* no-op */
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        /* no-op */
    }

    override fun afterTextChanged(s: Editable?) {
        /* no-op */
    }
}