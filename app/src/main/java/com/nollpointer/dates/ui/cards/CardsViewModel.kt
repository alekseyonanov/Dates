package com.nollpointer.dates.ui.cards

import androidx.lifecycle.MutableLiveData
import com.nollpointer.dates.other.BaseViewModel
import java.util.*
import javax.inject.Inject

class CardsViewModel @Inject constructor(
        private val dates: ArrayList<Date>,
        private val type: Int) : BaseViewModel() {

    //region LiveData
    val currentQuestion = MutableLiveData<Date>()
    //val historyLiveData = MutableLiveData<List<History>>()
    //endregion

    //region data

    //endregion

    /**
     * Действия при старте
     */
    override

    fun onStart() {

    }

    /**
     * Обработка нажатия на выбор секции
     *//*
    fun onSectionSelectClicked(view: View){
        navigator.navigateToSectionSeriesSelect()
    }

    */
    /**
     * Обработка нажатия на выбор места ремонта
     *//*
    fun onPlaceSelectClicked(view: View){
        navigator.navigateToPlaceSelect()
    }

    */
    /**
     * Обработка выбора конца периода
     *//*
    fun onActivityResultGot(requestCode: Int, resultCode: Int, data: Intent?){
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == 1) // PlaceSelect
                placeLiveData.value = data?.getParcelableExtra("DATA")
            if(requestCode == 1) // SectionSeries
                sectionSeriesLiveData.value = data?.getParcelableExtra("DATA")
        }

        Log.e("TAG", "onActivityResultGot: resultCode = $resultCode, requestCode = $requestCode, data = ${data?.data}" )

    }

    */
    /**
     * Обработка нажатия на историю
     *//*
    fun onHistoryClicked(history: History){
        //TODO доделать, когда появится страница Списка ЛО на ремонте
    }

    */
    /**
     * Обработка выбора начала периода
     *//*
    fun onStartPeriodSet(calendar: Calendar){
        startPeriodLiveData.value = calendar.timeInMillis
    }

    */
    /**
     * Обработка выбора конца периода
     *//*
    fun onEndPeriodSet(calendar: Calendar){
        endPeriodLiveData.value = calendar.timeInMillis
    }

    */
    /**
     * Обработка выбора конца периода
     *//*
    fun onSearchClicked(view: View){
        loadData()
    }

    */
    /**
     * Обработка ввода номер секции
     *//*
    fun onNumberQueryChanged(query: String){
        numberLiveData.value = query
    }
*/


}