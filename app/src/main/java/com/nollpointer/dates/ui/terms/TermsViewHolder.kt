package com.nollpointer.dates.ui.terms

import androidx.lifecycle.MutableLiveData
import com.nollpointer.dates.model.Term
import com.nollpointer.dates.other.BaseViewModel

/**
 * @author Onanov Aleksey (@onanov)
 */
class TermsViewHolder : BaseViewModel() {
    //region LiveData
    val termsLiveData = MutableLiveData<List<Term>>()
    val noDataLiveData = MutableLiveData<Boolean>()
    //endregion

    //region Data
    var terms = emptyList<Term>()
    //endregion

    /**
     * Действия при старте
     */
    override fun onStart() {

    }


}