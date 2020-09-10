package com.nollpointer.dates.other

import android.content.Context
import com.nollpointer.dates.ui.activity.MainActivity.Companion.FULL_DATES_MODE

/**
 * @author Onanov Aleksey (@onanov)
 */
object Loader {

    /*
    * Категории данных
    */
    private const val APP = "com.nollpointer.dates.app"
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

    fun isGDPRAgree(context: Context) = context.getSharedPreferences(APP, Context.MODE_PRIVATE).getBoolean(GDPR, false)

    fun setGdprAgree(context: Context, result: Boolean){
        context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putBoolean(GDPR, result).apply()
    }

    fun isFirstStart(context: Context) = context.getSharedPreferences(APP, Context.MODE_PRIVATE).getBoolean(FIRST_START, true)

    fun setFirstStart(context: Context, result: Boolean){
        context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putBoolean(FIRST_START, result).apply()
    }

    fun getMode(context: Context) = context.getSharedPreferences(APP, Context.MODE_PRIVATE).getInt(MODE, FULL_DATES_MODE)

    fun setMode(context: Context, mode: Int){
        context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putInt(MODE, mode).apply()
    }

    fun getDatesViewType(context: Context) = context.getSharedPreferences(APP, Context.MODE_PRIVATE).getInt(DATES_VIEW_TYPE, 0)

    fun setDatesViewType(context: Context, type: Int){
        context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putInt(DATES_VIEW_TYPE, type).apply()
    }

    fun getTermsViewType(context: Context) = context.getSharedPreferences(APP, Context.MODE_PRIVATE).getInt(TERMS_VIEW_TYPE, 0)

    fun setTermsViewType(context: Context, type: Int){
        context.getSharedPreferences(APP, Context.MODE_PRIVATE).edit().putInt(TERMS_VIEW_TYPE, type).apply()
    }


}