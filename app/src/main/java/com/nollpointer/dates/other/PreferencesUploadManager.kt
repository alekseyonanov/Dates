package com.nollpointer.dates.other

import android.content.Context
import android.os.AsyncTask

//TODO класс, который полностью отвечает за загрузку значений из памяти телефона. Работает асинхронно
class PreferencesUploadManager {

    operator fun get(context: Context, TAG: String) {}

    fun getAsync(TAG: String, listener: ()->Unit) {}

    operator fun set(TAG: String, obj: String) {}

    fun setAsync(TAG: String, obj: String, listener: ()->Unit) {}

    private inner class AsyncSet : AsyncTask<Void, Void?, Void>() {

        protected override fun doInBackground(vararg voids: Void): Void? {
            return null
        }
    }

    private inner class AsyncGet : AsyncTask<Void, Void?, Void>() {

        protected override fun doInBackground(vararg voids: Void): Void? {
            return null
        }
    }
}