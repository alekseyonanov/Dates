package com.nollpointer.dates.other;

import android.content.Context;
import android.os.AsyncTask;

//TODO класс, который полностью отвечает за загрузку значений из памяти телефона. Работает асинхронно
public class PreferencesUploadManager {

    static interface OnResultListener{

    }


    public static void get(Context context, String TAG){

    }

    public static void getAsync(String TAG, OnResultListener listener){

    }

    public static void set(String TAG, String object){

    }


    public static void setAsync(String TAG, String object, OnResultListener listener){

    }


    private class AsyncSet extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void aVoid) {
        }

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }
    }

    private class AsyncGet extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void aVoid) {
        }

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }
    }

}
