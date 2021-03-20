package com.nollpointer.dates.other

import android.content.Context
import com.nollpointer.dates.ui.activity.MainActivity.Companion.FULL_DATES_MODE
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Класс для работы с SharedPreferences
 *
 * @author Onanov Aleksey (@onanov)
 */
class Loader @Inject constructor(@ApplicationContext private val context: Context) {

    /*
    * Согласие GDPR
    */
    var isGdprAgree: Boolean
        set(value) = context.getSharedPreferences(ADS, Context.MODE_PRIVATE).edit().putBoolean(GDPR, value).apply()
        get() = context.getSharedPreferences(ADS, Context.MODE_PRIVATE).getBoolean(GDPR, false)

    /*
    * Флаг, отвечающий за первый старт приложения
    */
    var isFirstStart: Boolean
        set(value) = context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putBoolean(FIRST_START, value).apply()
        get() = context.getSharedPreferences(APP, Context.MODE_PRIVATE).getBoolean(FIRST_START, true)

    /*
    * Режим приложения
    */
    var mode: Int
        set(value) = context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putInt(MODE, value).apply()
        get() = context.getSharedPreferences(APP, Context.MODE_PRIVATE).getInt(MODE, FULL_DATES_MODE)

    /*
    * Тип отображения дат
    */
    var datesViewType: Int
        set(value) = context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putInt(DATES_VIEW_TYPE, value).apply()
        get() = context.getSharedPreferences(APP, Context.MODE_PRIVATE).getInt(DATES_VIEW_TYPE, 0)

    /*
    * Тип отображения терминов
    */
    var termsViewType: Int
        set(value) = context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putInt(TERMS_VIEW_TYPE, value).apply()
        get() = context.getSharedPreferences(APP, Context.MODE_PRIVATE).getInt(TERMS_VIEW_TYPE, 0)

    /*
    * Функция очистки
    */
    fun clear() {
        context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().clear().apply()
        //TODO подумать насчет согласия GDPR
    }

    companion object {
        /*
        * Категории данных
        */
        private const val APP = "com.nollpointer.dates.app"
        private const val ADS = "com.nollpointer.dates.ads"
        private const val PRACTISE = "com.nollpointer.dates.practise"
        private const val SETTINGS = "com.nollpointer.dates.settings"

        /*
        * Наименование полей
        */
        private const val FIRST_START = "first_start"
        private const val GDPR = "gdpr"
        private const val MODE = "mode"
        private const val DATES_VIEW_TYPE = "dates_view_type"
        private const val TERMS_VIEW_TYPE = "terms_view_type"
    }

}