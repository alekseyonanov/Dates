package com.nollpointer.dates.model

import androidx.fragment.app.Fragment
import com.nollpointer.dates.other.ScrollableFragment

/**
 * @author Onanov Aleksey (@onanov)
 */
class FragmentPage(val title: String, var fragment: Fragment, val type: Int = -1) {

    val isScrollable: Boolean
        get() = fragment is ScrollableFragment

    fun scrollTop() {
        (fragment as ScrollableFragment).scrollTop()
    }

    companion object{
        const val PRACTISE_DATES = 0
        const val PRACTISE_TERMS = 1
    }

}
